package org.springdoc.core;

import static org.springdoc.core.Constants.HEADER_PARAM;
import static org.springdoc.core.Constants.PATH_PARAM;
import static org.springdoc.core.Constants.QUERY_PARAM;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;

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
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;

@Component
public class RequestBuilder {

	public Operation build(Components components, HandlerMethod handlerMethod, RequestMethod requestMethod,
			Operation operation, String[] allConsumes) {
		// Documentation
		operation.setOperationId(handlerMethod.getMethod().getName());
		// requests
		LocalVariableTableParameterNameDiscoverer d = new LocalVariableTableParameterNameDiscoverer();
		String[] pNames = d.getParameterNames(handlerMethod.getMethod());
		List<Parameter> operationParameters = new ArrayList<>();
		java.lang.reflect.Parameter[] parameters = handlerMethod.getMethod().getParameters();

		for (int i = 0; i < pNames.length; i++) {
			// check if query param
			Parameter parameter = null;
			io.swagger.v3.oas.annotations.Parameter parameterDoc = AnnotationUtils.findAnnotation(parameters[i],
					io.swagger.v3.oas.annotations.Parameter.class);

			// use documentation as reference
			if (parameterDoc != null) {
				if (parameterDoc.hidden()) {
					continue;
				}
				parameter = buildParameterFromDoc(parameterDoc);
			}

			parameter = buildParams(components, parameters[i], parameter);

			// By default
			if (RequestMethod.GET.equals(requestMethod) && parameter == null) {
				String name = pNames[i];
				parameter = this.buildParam(QUERY_PARAM, null, parameters[i], Boolean.FALSE, name, null);
			}

			if (parameter != null && parameter.getName() != null) {
				applyBeanValidatorAnnotations(parameter, Arrays.asList(parameters[i].getAnnotations()));
				operationParameters.add(parameter);
			}
			else if (!RequestMethod.GET.equals(requestMethod)) {
				RequestBody requestBody = buildRequestBody(components, allConsumes, parameters[i], parameterDoc);
				operation.setRequestBody(requestBody);
			}
		}

		if (!CollectionUtils.isEmpty(operationParameters)) {
			operation.setParameters(operationParameters);
		}

		return operation;
	}

	private Parameter buildParams(Components components, java.lang.reflect.Parameter parameters, Parameter parameter) {
		RequestHeader requestHeader = AnnotationUtils.findAnnotation(parameters, RequestHeader.class);
		RequestParam requestParam = AnnotationUtils.findAnnotation(parameters, RequestParam.class);
		PathVariable pathVar = AnnotationUtils.findAnnotation(parameters, PathVariable.class);

		if (requestHeader != null) {
			parameter = this.buildParam(HEADER_PARAM, components, parameters, requestHeader.required(),
					requestHeader.value(), parameter);
		} else if (requestParam != null) {
			parameter = this.buildParam(QUERY_PARAM, components, parameters, requestParam.required(),
					requestParam.value(), parameter);
		} else if (pathVar != null) {
			// check if PATH PARAM
			parameter = this.buildParam(PATH_PARAM, components, parameters, Boolean.TRUE, pathVar.value(), parameter);
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
		Schema<?> schema = calculateSchema(components, parameters);
		parameter.setSchema(schema);
		return parameter;
	}

	private RequestBody buildRequestBody(Components components, String[] allConsumes,
			java.lang.reflect.Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterDoc) {
		RequestBody requestBody = new RequestBody();
		Schema<?> schema = calculateSchema(components, parameter);
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

	private Parameter buildParameterFromDoc(io.swagger.v3.oas.annotations.Parameter parameterDoc) {
		Parameter parameter = new Parameter();
		if (StringUtils.isNotBlank(parameterDoc.description())) {
			parameter.setDescription(parameterDoc.description());
		} else if (StringUtils.isNotBlank(parameterDoc.name())) {
			parameter.setDescription(parameterDoc.name());
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

	private Schema<?> calculateSchema(Components components, java.lang.reflect.Parameter parameter) {
		Schema<?> schemaN = null;
		Type returnType = parameter.getParameterizedType();
		if (returnType instanceof ParameterizedType) {
			ResolvedSchema resolvedSchema = ModelConverters.getInstance()
					.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
			if (resolvedSchema.schema != null) {
				schemaN = resolvedSchema.schema;
				@SuppressWarnings("rawtypes")
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
			@SuppressWarnings("rawtypes")
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
	private void applyBeanValidatorAnnotations(final Parameter parameter, final List<Annotation> annotations) {
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

	private void setMediaTypeToContent(@SuppressWarnings("rawtypes") Schema schema, Content content, String value) {
		io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
		mediaTypeObject.setSchema(schema);
		content.addMediaType(value, mediaTypeObject);
	}
}
