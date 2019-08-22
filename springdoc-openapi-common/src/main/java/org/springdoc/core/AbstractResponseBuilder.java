package org.springdoc.core;

import static org.springdoc.core.Constants.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
public abstract class AbstractResponseBuilder {

	private Map<String, ApiResponse> genericMapResponse = new HashMap<>();

	private OperationBuilder operationBuilder;

	protected AbstractResponseBuilder(OperationBuilder operationBuilder) {
		super();
		this.operationBuilder = operationBuilder;
	}

	public ApiResponses build(Components components, HandlerMethod handlerMethod, Operation operation,
			String[] methodProduces) {
		ApiResponses apiResponses = operation.getResponses();
		if (apiResponses == null)
			apiResponses = new ApiResponses();

		// for each one build ApiResponse and add it to existing responses
		for (Entry<String, ApiResponse> entry : genericMapResponse.entrySet()) {
			apiResponses.addApiResponse(entry.getKey(), entry.getValue());
		}
		// Fill api Responses
		computeResponse(components, handlerMethod.getMethod(), apiResponses, methodProduces, false);

		return apiResponses;
	}

	public void buildGenericResponse(Components components, Map<String, Object> findControllerAdvice) {
		// ControllerAdvice
		List<Method> methods = getMethods(findControllerAdvice);

		// for each one build ApiResponse and add it to existing responses
		for (Method method : methods) {
			if (!operationBuilder.isHidden(method)) {
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
		io.swagger.v3.oas.annotations.responses.ApiResponse[] responsesArray = getApiResponses(method);
		if (ArrayUtils.isNotEmpty(responsesArray)) {
			for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponse2 : responsesArray) {
				ApiResponse apiResponse1 = new ApiResponse();
				apiResponse1.setDescription(apiResponse2.description());
				io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponse2.content();
				org.springdoc.core.SpringDocAnnotationsUtils.getContent(contentdoc, new String[0],
						methodProduces == null ? new String[0] : methodProduces, null, components, null)
						.ifPresent(apiResponse1::content);
				AnnotationsUtils.getHeaders(apiResponse2.headers(), null).ifPresent(apiResponse1::headers);
				apiResponsesOp.addApiResponse(apiResponse2.responseCode(), apiResponse1);
			}
		}

		if (!CollectionUtils.isEmpty(apiResponsesOp) && (apiResponsesOp.size() != genericMapResponse.size())) {
			// API Responses at operation and apiresposne annotation
			for (Map.Entry<String, ApiResponse> entry : apiResponsesOp.entrySet()) {
				String httpCode = entry.getKey();
				ApiResponse apiResponse = entry.getValue();
				buildApiResponses(components, method, apiResponsesOp, methodProduces, httpCode, apiResponse);
			}
		} else {
			// Use reponse parameters with no descirption filled - No documentation
			// available
			String httpCode = evaluateResponseStatus(method, method.getClass(), isGeneric);
			if (httpCode != null)
				buildApiResponses(components, method, apiResponsesOp, methodProduces, httpCode, new ApiResponse());
		}
		return apiResponsesOp;
	}

	private io.swagger.v3.oas.annotations.responses.ApiResponse[] getApiResponses(Method method) {
		io.swagger.v3.oas.annotations.responses.ApiResponse[] responsesArray = null;
		io.swagger.v3.oas.annotations.responses.ApiResponses apiResponsesDoc = ReflectionUtils.getAnnotation(method,
				io.swagger.v3.oas.annotations.responses.ApiResponses.class);
		if (apiResponsesDoc != null) {
			responsesArray = apiResponsesDoc.value();
		} else {
			List<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseDoc = ReflectionUtils
					.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.responses.ApiResponse.class);
			if (!CollectionUtils.isEmpty(apiResponseDoc)) {
				responsesArray = apiResponseDoc.stream()
						.toArray(io.swagger.v3.oas.annotations.responses.ApiResponse[]::new);
			}
		}
		return responsesArray;
	}

	protected abstract Content buildContent(Components components, Method method, String[] methodProduces);

	protected Schema calculateSchemaParameterizedType(Components components, ParameterizedType parameterizedType) {
		Schema schemaN = null;
		if (parameterizedType.getActualTypeArguments()[0] instanceof Class
				&& !Void.class.equals(parameterizedType.getActualTypeArguments()[0])) {
			schemaN = calculateSchema(components, parameterizedType);
		} else if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType
				&& !Void.class.equals(parameterizedType.getActualTypeArguments()[0])) {
			parameterizedType = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
			schemaN = calculateSchemaForActualTypeArguments(components, parameterizedType);
		} else if (Void.class.equals(parameterizedType.getActualTypeArguments()[0])) {
			// if void, no content
			schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
		}
		return schemaN;
	}

	protected void setContent(String[] methodProduces, Content content,
			io.swagger.v3.oas.models.media.MediaType mediaType) {
		if (ArrayUtils.isNotEmpty(methodProduces)) {
			for (String mediaTypeStr : methodProduces) {
				content.addMediaType(mediaTypeStr, mediaType);
			}
		} else if (content.size() == 0) {
			content.addMediaType(MediaType.ALL_VALUE, mediaType);
		}
	}

	protected Schema extractSchema(Components components, Type returnType) {
		Schema schemaN = null;
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

	private Schema calculateSchemaForActualTypeArguments(Components components, ParameterizedType parameterizedType) {
		Schema schemaN = null;
		ResolvedSchema resolvedSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(new AnnotatedType(parameterizedType).resolveAsRef(true));
		if (resolvedSchema.schema != null) {
			schemaN = resolvedSchema.schema;
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach(components::addSchemas);
			}
		}
		return schemaN;
	}

	private Schema calculateSchema(Components components, ParameterizedType parameterizedType) {
		Schema schemaN;
		schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) parameterizedType.getActualTypeArguments()[0], null,
				null);
		if (schemaN.getType() == null) {
			schemaN = this.extractSchema(components, parameterizedType.getActualTypeArguments()[0]);
		}
		return schemaN;
	}

	private void buildApiResponses(Components components, Method method, ApiResponses apiResponsesOp,
			String[] methodProduces, String httpCode, ApiResponse apiResponse) {
		// No documentation
		if (apiResponse.getContent() == null) {
			Content content = buildContent(components, method, methodProduces);
			apiResponse.setContent(content);
		}
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
			responseStatus = String.valueOf(annotation.code().value());
		}
		if (annotation == null && !isGeneric) {
			responseStatus = String.valueOf(HttpStatus.OK.value());
		}
		return responseStatus;
	}

}
