package org.springdoc.core;

import static org.springdoc.core.Constants.HEADER_PARAM;
import static org.springdoc.core.Constants.PATH_PARAM;
import static org.springdoc.core.Constants.QUERY_PARAM;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;

@SuppressWarnings({ "rawtypes" })
public abstract class AbstractRequestBuilder {

	public abstract Operation build(Components components, HandlerMethod handlerMethod, RequestMethod requestMethod,
			Operation operation, String[] allConsumes);

	protected <A extends Annotation> A getParameterAnnotation(HandlerMethod handlerMethod,
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

	protected Parameter buildParamDefault(RequestMethod requestMethod, String pNames,
			java.lang.reflect.Parameter parameters, Parameter parameter) {
		if (RequestMethod.GET.equals(requestMethod) && parameter == null) {
			parameter = this.buildParam(QUERY_PARAM, null, parameters, Boolean.FALSE, pNames, null);
		}
		return parameter;
	}

	protected Parameter buildParams(String pName, Components components, java.lang.reflect.Parameter parameters,
			int index, Parameter parameter, HandlerMethod handlerMethod) {
		RequestHeader requestHeader = getParameterAnnotation(handlerMethod, parameters, index, RequestHeader.class);
		RequestParam requestParam = getParameterAnnotation(handlerMethod, parameters, index, RequestParam.class);
		PathVariable pathVar = getParameterAnnotation(handlerMethod, parameters, index, PathVariable.class);

		if (requestHeader != null) {
			String name = StringUtils.isBlank(requestHeader.value()) ? pName : requestHeader.value();
			parameter = this.buildParam(HEADER_PARAM, components, parameters, requestHeader.required(), name,
					parameter);
		} else if (requestParam != null) {
			String name = StringUtils.isBlank(requestParam.value()) ? pName : requestParam.value();
			parameter = this.buildParam(QUERY_PARAM, components, parameters, requestParam.required(), name, parameter);
		} else if (pathVar != null) {
			String name = StringUtils.isBlank(pathVar.value()) ? pName : pathVar.value();
			// check if PATH PARAM
			parameter = this.buildParam(PATH_PARAM, components, parameters, Boolean.TRUE, name, parameter);
		}
		return parameter;
	}

	private Parameter buildParam(String in, Components components, java.lang.reflect.Parameter parameters,
			Boolean required, String name, Parameter parameter) {
		if (parameter == null)
			parameter = new Parameter();
		parameter.setIn(in);
		parameter.setRequired(required);
		parameter.setName(name);
		Schema<?> schema = calculateSchema(components, parameters, name);
		parameter.setSchema(schema);
		return parameter;
	}

	protected RequestBody buildRequestBody(Components components, String[] allConsumes,
			java.lang.reflect.Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterDoc,
			String paramName) {
		RequestBody requestBody = new RequestBody();

		Schema<?> schema = calculateSchema(components, parameter, paramName);
		io.swagger.v3.oas.models.media.MediaType mediaType = null;
		if (schema != null && schema.getType() != null) {
			mediaType = new io.swagger.v3.oas.models.media.MediaType();
			mediaType.setSchema(schema);
		} else {
			Type returnType = parameter.getType();
			mediaType = calculateSchema(components, returnType);
		}

		Content content1 = new Content();
		if (ArrayUtils.isNotEmpty(allConsumes)) {
			for (String value : allConsumes) {
				setMediaTypeToContent(schema, content1, value);
			}
		} else {
			content1.addMediaType(MediaType.ALL_VALUE, mediaType);
		}
		requestBody.setContent(content1);
		if (parameterDoc != null) {
			if (StringUtils.isNotBlank(parameterDoc.description()))
				requestBody.setDescription(parameterDoc.description());
			requestBody.setRequired(parameterDoc.required());
		}
		return requestBody;
	}

	protected Parameter buildParameterFromDoc(io.swagger.v3.oas.annotations.Parameter parameterDoc) {
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
		}

		setExamples(parameterDoc, parameter);
		setExtensions(parameterDoc, parameter);
		setParameterStyle(parameter, parameterDoc);
		setParameterExplode(parameter, parameterDoc);
		return parameter;
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

	private Schema<?> calculateSchema(Components components, java.lang.reflect.Parameter parameter, String paramName) {
		Schema<?> schemaN = null;
		Type returnType = parameter.getParameterizedType();

		JavaType ct = constructType(parameter.getType());

		if (MultipartFile.class.isAssignableFrom(ct.getRawClass())) {
			schemaN = new ObjectSchema();
			schemaN.addProperties(paramName, new FileSchema());
			return schemaN;
		}

		if (returnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) returnType;
			if (parameterizedType.getActualTypeArguments()[0].getTypeName().equals(MultipartFile.class.getName())) {
				schemaN = new ObjectSchema();
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
				if (schemaMap != null) {
					schemaMap.forEach(components::addSchemas);
				}
			}
		} else {
			schemaN = org.springdoc.core.AnnotationsUtils.resolveSchemaFromType(parameter.getType(), null, null);
		}
		return schemaN;
	}

	private io.swagger.v3.oas.models.media.MediaType calculateSchema(Components components, Type returnType) {
		ResolvedSchema resolvedSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
		io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
		if (resolvedSchema.schema != null) {
			Schema<?> returnTypeSchema = resolvedSchema.schema;
			if (returnTypeSchema != null) {
				mediaType.setSchema(returnTypeSchema);
			}
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach(components::addSchemas);
			}
		}
		return mediaType;
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

	private void setParameterStyle(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
		if (StringUtils.isNotBlank(p.style().toString())) {
			parameter.setStyle(Parameter.StyleEnum.valueOf(p.style().toString().toUpperCase()));
		}
	}

	/**
	 * This is mostly a duplicate of
	 * {@link io.swagger.v3.core.jackson.ModelResolver#applyBeanValidatorAnnotations}.
	 *
	 * @param parameter
	 * @param annotations
	 */
	protected void applyBeanValidatorAnnotations(final Parameter parameter, final List<Annotation> annotations) {
		Map<String, Annotation> annos = new HashMap<>();
		if (annotations != null) {
			annotations.forEach(annotation -> annos.put(annotation.annotationType().getName(), annotation));
		}

		if (annos.containsKey(NotNull.class.getName())) {
			parameter.setRequired(true);
		}

		Schema<?> schema = parameter.getSchema();

		if (annos.containsKey(Min.class.getName())) {
			Min min = (Min) annos.get(Min.class.getName());
			schema.setMinimum(BigDecimal.valueOf(min.value()));
		}
		if (annos.containsKey(Max.class.getName())) {
			Max max = (Max) annos.get(Max.class.getName());
			schema.setMaximum(BigDecimal.valueOf(max.value()));
		}
		if (annos.containsKey(Size.class.getName())) {
			Size size = (Size) annos.get(Size.class.getName());

			schema.setMinimum(BigDecimal.valueOf(size.min()));
			schema.setMaximum(BigDecimal.valueOf(size.max()));

			schema.setMinItems(size.min());
			schema.setMaxItems(size.max());
		}
		if (annos.containsKey(DecimalMin.class.getName())) {
			DecimalMin min = (DecimalMin) annos.get(DecimalMin.class.getName());
			if (min.inclusive()) {
				schema.setMinimum(BigDecimal.valueOf(Double.valueOf(min.value())));
			} else {
				schema.setExclusiveMinimum(!min.inclusive());
			}
		}
		if (annos.containsKey(DecimalMax.class.getName())) {
			DecimalMax max = (DecimalMax) annos.get(DecimalMax.class.getName());
			if (max.inclusive()) {
				schema.setMaximum(BigDecimal.valueOf(Double.valueOf(max.value())));
			} else {
				schema.setExclusiveMaximum(!max.inclusive());
			}
		}
		if (annos.containsKey(Pattern.class.getName())) {
			Pattern pattern = (Pattern) annos.get(Pattern.class.getName());
			schema.setPattern(pattern.regexp());
		}
	}

	private void setMediaTypeToContent(Schema schema, Content content, String value) {
		io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
		mediaTypeObject.setSchema(schema);
		content.addMediaType(value, mediaTypeObject);
	}

	protected JavaType constructType(Type type) {
		return TypeFactory.defaultInstance().constructType(type);
	}
}
