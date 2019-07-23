package org.springdoc.core;

import static org.springdoc.core.Constants.DEFAULT_DESCRIPTION;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Component
@SuppressWarnings("rawtypes")
public class ResponseBuilder {

	private Map<String, ApiResponse> genericMapResponse = new HashMap<>();

	public ApiResponses build(Components components, HandlerMethod handlerMethod, Operation operation,
			String[] methodProduces) {
		ApiResponses apiResponses = operation.getResponses();
		if (apiResponses == null)
			apiResponses = new ApiResponses();
		// Fill api Responses
		computeResponse(components, handlerMethod.getMethod(), apiResponses, methodProduces, false);

		// for each one build ApiResponse and add it to existing responses
		for (Entry<String, ApiResponse> entry : genericMapResponse.entrySet()) {
			apiResponses.addApiResponse(entry.getKey(), entry.getValue());
		}
		return apiResponses;
	}

	public void buildGenericResponse(Components components, Map<String, Object> findControllerAdvice) {
		// ControllerAdvice
		List<Method> methods = getMethods(findControllerAdvice);

		// for each one build ApiResponse and add it to existing responses
		for (Method method : methods) {
			RequestMapping reqMappringMethod = ReflectionUtils.getAnnotation(method, RequestMapping.class);
			String[] methodProduces = null;
			if (reqMappringMethod != null) {
				methodProduces = reqMappringMethod.produces();
			}
			Map<String, ApiResponse> apiResponses = computeResponse(components, method, new ApiResponses(),
					methodProduces, true);
			for (Map.Entry<String, ApiResponse> entry : apiResponses.entrySet()) {
					genericMapResponse.put(entry.getKey(), entry.getValue());
			}
		}

	}

	private List<Method> getMethods(Map<String, Object> findControllerAdvice) {
		List<Method> methods = new ArrayList<>();
		for (Map.Entry<String, Object> entry : findControllerAdvice.entrySet()) {
			Object controllerAdvice = entry.getValue();
			// get all methods with annotation @ExceptionHandler
			Class<?> objClz = controllerAdvice.getClass();
			if (org.springframework.aop.support.AopUtils.isAopProxy(controllerAdvice)) {
				objClz = org.springframework.aop.support.AopUtils.getTargetClass(controllerAdvice);
			}
			for (Method m : objClz.getDeclaredMethods()) {
				if (m.isAnnotationPresent(ExceptionHandler.class)) {
					methods.add(m);
				}
			}
		}
		return methods;
	}

	private Map<String, ApiResponse> computeResponse(Components components, Method method, ApiResponses apiResponsesOp,
			String[] methodProduces, boolean isGeneric) {
		// Parsing documentation, if present
		io.swagger.v3.oas.annotations.responses.ApiResponses apiResponsesDoc = ReflectionUtils.getAnnotation(method,
				io.swagger.v3.oas.annotations.responses.ApiResponses.class);
		if (apiResponsesDoc != null) {
			io.swagger.v3.oas.annotations.responses.ApiResponse[] responsesArray = apiResponsesDoc.value();
			for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponse2 : responsesArray) {
				ApiResponse apiResponse1 = new ApiResponse();
				apiResponse1.setDescription(apiResponse2.description());
				io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponse2.content();
				Content contentElt = null;
				for (int i = 0; i < contentdoc.length; i++) {
					contentElt = new Content();
					io.swagger.v3.oas.models.media.MediaType mediaTypeEl = new io.swagger.v3.oas.models.media.MediaType();
					AnnotationsUtils.getSchema(contentdoc[i], components, null).orElse(null);
					mediaTypeEl.schema(AnnotationsUtils.getSchema(contentdoc[i], components, null).orElse(null));
					setContent(methodProduces, contentElt, mediaTypeEl);
				}
				apiResponse1.content(contentElt);
				apiResponsesOp.addApiResponse(apiResponse2.responseCode(), apiResponse1);
			}
		}

		if (!CollectionUtils.isEmpty(apiResponsesOp)) { // API Responses at operation and apiresposne annotation
			for (Map.Entry<String, ApiResponse> entry : apiResponsesOp.entrySet()) {
				String httpCode = entry.getKey();
				ApiResponse apiResponse = entry.getValue();
				buildApiResponses(components, method, apiResponsesOp, methodProduces, httpCode, apiResponse);
			}
		} else {
			// Use reponse parameters with no descirption filled
			String httpCode = evaluateResponseStatus(method, method.getClass(), isGeneric);
			if (httpCode != null)
				buildApiResponses(components, method, apiResponsesOp, methodProduces, httpCode, new ApiResponse());
		}
		return apiResponsesOp;
	}

	private Content buildContent(Components components, Method method, String[] methodProduces) {
		Content content = new Content();
		Schema<?> schemaN = null;
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) returnType;
			if (ResponseEntity.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
				schemaN = calculateSchemaParameterizedType(components, parameterizedType);
			}
		} else if (returnType instanceof TypeVariable) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
		} else if (Void.TYPE.equals(returnType)) {
			// if void, no content
			schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
		}
		if (schemaN == null) {
			schemaN = extractSchema(components, returnType);
		}

		if (schemaN == null && returnType instanceof Class) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
		}

		if (schemaN != null) {
			io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
			mediaType.setSchema(schemaN);
			// Fill the content
			setContent(methodProduces, content, mediaType);
		}
		return content;
	}

	private Schema<?> calculateSchemaParameterizedType(Components components, ParameterizedType parameterizedType) {
		Schema<?> schemaN = null;
		if (parameterizedType.getActualTypeArguments()[0] instanceof Class
				&& !Void.class.equals(parameterizedType.getActualTypeArguments()[0])) {
			schemaN = calculateSchema(components, parameterizedType);
		} else if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType
				&& !Void.class.equals(parameterizedType.getActualTypeArguments()[0])) {
			parameterizedType = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
			schemaN = calculateSchema(components, parameterizedType);
		} else if (Void.class.equals(parameterizedType.getActualTypeArguments()[0])) {
			// if void, no content
			schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
		}
		return schemaN;
	}

	private void setContent(String[] methodProduces, Content content,
			io.swagger.v3.oas.models.media.MediaType mediaType) {
		if (ArrayUtils.isNotEmpty(methodProduces)) {
			for (String mediaType2 : methodProduces) {
				content.addMediaType(mediaType2, mediaType);
			}
		} else if (content.size() == 0) {
			content.addMediaType(MediaType.ALL_VALUE, mediaType);
		}
	}

	private Schema<?> extractSchema(Components components, Type returnType) {
		Schema<?> schemaN = null;
		ResolvedSchema resolvedSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
		if (resolvedSchema.schema != null) {
			schemaN = resolvedSchema.schema;
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach(components::addSchemas);
			}
		}
		return schemaN;
	}

	private Schema<?> calculateSchema(Components components, ParameterizedType parameterizedType) {
		Schema<?> schemaN;
		schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) parameterizedType.getActualTypeArguments()[0], null,
				null);
		if (schemaN.getType() == null) {
			schemaN = this.extractSchema(components, parameterizedType.getActualTypeArguments()[0]);
		}
		return schemaN;
	}

	private void buildApiResponses(Components components, Method method, ApiResponses apiResponsesOp,
			String[] methodProduces, String httpCode, ApiResponse apiResponse) {
		Content content = buildContent(components, method, methodProduces);
		apiResponse.setContent(content);

		if (StringUtils.isBlank(apiResponse.getDescription())) {
			apiResponse.setDescription(DEFAULT_DESCRIPTION);
		}
		apiResponsesOp.addApiResponse(httpCode, apiResponse);
	}

	private String evaluateResponseStatus(Method method, Class<?> beanType, boolean isGeneric) {
		String responseStatus = null;
		ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(method, ResponseStatus.class);
		if (annotation == null && beanType != null) {
			annotation = AnnotatedElementUtils.findMergedAnnotation(beanType, ResponseStatus.class);
		}
		if (annotation != null) {
			responseStatus = annotation.code().toString();
		}
		if (annotation == null && !isGeneric) {
			responseStatus = HttpStatus.OK.toString();
		}
		return responseStatus;
	}

}
