package org.springdoc.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

@Component
@SuppressWarnings("rawtypes")
public class ParameterBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterBuilder.class);

	public Parameter buildParameterFromDoc(io.swagger.v3.oas.annotations.Parameter parameterDoc,
			Components components) {
		Parameter parameter = new Parameter();
		if (StringUtils.isNotBlank(parameterDoc.description())) {
			parameter.setDescription(parameterDoc.description());
		}
		if (StringUtils.isNotBlank(parameterDoc.name())) {
			parameter.setName(parameterDoc.name());
		}
		if (StringUtils.isNotBlank(parameterDoc.in().toString())) {
			parameter.setIn(parameterDoc.in().toString());
		}
		if (StringUtils.isNotBlank(parameterDoc.example())) {
			try {
				parameter.setExample(Json.mapper().readTree(parameterDoc.example()));
			} catch (IOException e) {
				parameter.setExample(parameterDoc.example());
			}
		}
		if (parameterDoc.deprecated()) {
			parameter.setDeprecated(parameterDoc.deprecated());
		}
		if (parameterDoc.required()) {
			parameter.setRequired(parameterDoc.required());
		}
		if (parameterDoc.allowEmptyValue()) {
			parameter.setAllowEmptyValue(parameterDoc.allowEmptyValue());
		}
		if (parameterDoc.allowReserved()) {
			parameter.setAllowReserved(parameterDoc.allowReserved());
		}
		if (StringUtils.isNotBlank(parameterDoc.ref())) {
			parameter.$ref(parameterDoc.ref());
		} else {
			Type type = ParameterProcessor.getParameterType(parameterDoc);
			Schema schema = null;
			if (parameterDoc.schema() != null)
				schema = AnnotationsUtils.getSchemaFromAnnotation(parameterDoc.schema(), components, null).orElse(null);
			else
				schema = this.calculateSchema(components, null, parameter.getName(), type, null);
			parameter.setSchema(schema);
		}

		setExamples(parameterDoc, parameter);
		setExtensions(parameterDoc, parameter);
		setParameterStyle(parameter, parameterDoc);
		setParameterExplode(parameter, parameterDoc);

		return parameter;
	}

	public Schema calculateSchema(Components components, java.lang.reflect.Parameter parameter, String paramName,
			Type type, Schema mergedSchema) {

		Schema schemaN = null;
		Class<?> schemaImplementation = null;
		Type returnType;
		JavaType ct;

		if (parameter != null) {
			returnType = parameter.getParameterizedType();
			ct = constructType(parameter.getType());
			schemaImplementation = parameter.getType();
		} else {
			returnType = type;
			ct = constructType(type);
			try {
				schemaImplementation = Class.forName(returnType.getTypeName());
			} catch (ClassNotFoundException e) {
				LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
			}
		}

		if (MultipartFile.class.isAssignableFrom(ct.getRawClass())) {
			schemaN = (mergedSchema != null) ? mergedSchema : new ObjectSchema();
			schemaN.addProperties(paramName, new FileSchema());
			return schemaN;
		}

		if (returnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) returnType;
			if (parameterizedType.getActualTypeArguments()[0].getTypeName().equals(MultipartFile.class.getName())) {
				schemaN = (mergedSchema != null) ? mergedSchema : new ObjectSchema();
				ArraySchema schemafile = new ArraySchema();
				schemafile.items(new FileSchema());
				schemaN.addProperties(paramName, new ArraySchema().items(new FileSchema()));
				return schemaN;
			}

			ResolvedSchema resolvedSchema = ModelConverters.getInstance()
					.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));

			if (resolvedSchema.schema != null) {
				schemaN = resolvedSchema.schema;
				Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
				if (schemaMap != null)
					schemaMap.forEach(components::addSchemas);
			}
		} else {
			schemaN = org.springdoc.core.SpringDocAnnotationsUtils.resolveSchemaFromType(schemaImplementation,
					components, null);
		}
		if (mergedSchema != null) {
			mergedSchema.addProperties(paramName, schemaN);
			schemaN = mergedSchema;
		}

		return schemaN;
	}

	public <A extends Annotation> A getParameterAnnotation(HandlerMethod handlerMethod,
			java.lang.reflect.Parameter parameter, int i, Class<A> annotationType) {
		A parameterDoc = AnnotationUtils.getAnnotation(parameter, annotationType);
		if (parameterDoc == null) {
			Set<Method> methods = MethodUtils.getOverrideHierarchy(handlerMethod.getMethod(),
					ClassUtils.Interfaces.INCLUDE);
			for (Method methodOverriden : methods) {
				parameterDoc = AnnotationUtils.getAnnotation(methodOverriden.getParameters()[i], annotationType);
				if (parameterDoc != null)
					break;
			}
		}
		return parameterDoc;
	}

	private void setExamples(io.swagger.v3.oas.annotations.Parameter parameterDoc, Parameter parameter) {
		Map<String, Example> exampleMap = new HashMap<>();
		if (parameterDoc.examples().length == 1 && StringUtils.isBlank(parameterDoc.examples()[0].name())) {
			Optional<Example> exampleOptional = AnnotationsUtils.getExample(parameterDoc.examples()[0]);
			if (exampleOptional.isPresent()) {
				parameter.setExample(exampleOptional.get());
			}
		} else {
			for (ExampleObject exampleObject : parameterDoc.examples()) {
				AnnotationsUtils.getExample(exampleObject)
						.ifPresent(example -> exampleMap.put(exampleObject.name(), example));
			}
		}
		if (exampleMap.size() > 0) {
			parameter.setExamples(exampleMap);
		}
	}

	private void setExtensions(io.swagger.v3.oas.annotations.Parameter parameterDoc, Parameter parameter) {
		if (parameterDoc.extensions().length > 0) {
			Map<String, Object> extensionMap = AnnotationsUtils.getExtensions(parameterDoc.extensions());
			for (Map.Entry<String, Object> entry : extensionMap.entrySet()) {
				parameter.addExtension(entry.getKey(), entry.getValue());
			}
		}
	}

	private void setParameterExplode(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
		if (isExplodable(p)) {
			if (Explode.TRUE.equals(p.explode())) {
				parameter.setExplode(Boolean.TRUE);
			} else if (Explode.FALSE.equals(p.explode())) {
				parameter.setExplode(Boolean.FALSE);
			}
		}
	}

	private void setParameterStyle(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
		if (StringUtils.isNotBlank(p.style().toString())) {
			parameter.setStyle(Parameter.StyleEnum.valueOf(p.style().toString().toUpperCase()));
		}
	}

	private boolean isExplodable(io.swagger.v3.oas.annotations.Parameter p) {
		io.swagger.v3.oas.annotations.media.Schema schema = p.schema();
		boolean explode = true;
		if (schema != null) {
			Class<?> implementation = schema.implementation();
			if (implementation == Void.class && !schema.type().equals("object") && !schema.type().equals("array")) {
				explode = false;
			}
		}
		return explode;
	}

	private JavaType constructType(Type type) {
		return TypeFactory.defaultInstance().constructType(type);
	}
}
