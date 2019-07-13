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
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Component
public class ResponseBuilder {

	private Map<String, ApiResponse> genericMapResponse;

	public ApiResponses build(Components components, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod,
			Operation operation, String[] classProduces, String[] methodProduces) throws ClassNotFoundException {

		ApiResponses apiResponses = new ApiResponses();

		// for each one build ApiResponse and add it to existing responses
		if (!CollectionUtils.isEmpty(genericMapResponse)) {
			for (Entry<String, ApiResponse> entry : genericMapResponse.entrySet()) {
				apiResponses.addApiResponse(entry.getKey(), entry.getValue());
			}
		}

		ApiResponse apiResponse = new ApiResponse();
		Content content = new Content();
		io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();

		// TODO JSON VIEW
		Schema<?> schemaN = null;
		Type returnType = handlerMethod.getMethod().getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) returnType;
			if (ResponseEntity.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
				if (parameterizedType.getActualTypeArguments()[0] instanceof Class
						&& !Void.class.getName().equals(parameterizedType.getActualTypeArguments()[0].getTypeName())) {
					schemaN = AnnotationsUtils.resolveSchemaFromType(
							(Class<?>) parameterizedType.getActualTypeArguments()[0], null, null);
					if (schemaN.getType() == null) {
						ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(
								new AnnotatedType(parameterizedType.getActualTypeArguments()[0]).resolveAsRef(true));
						if (resolvedSchema.schema != null) {
							schemaN = resolvedSchema.schema;
							@SuppressWarnings("rawtypes")
							Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
							if (schemaMap != null) {
								schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
							}
						}
					}
				} else if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType
						&& !Void.class.getName().equals(parameterizedType.getActualTypeArguments()[0].getTypeName())) {
					parameterizedType = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
					schemaN = AnnotationsUtils.resolveSchemaFromType(
							(Class<?>) parameterizedType.getActualTypeArguments()[0], null, null);
					if (schemaN.getType() == null) {
						ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(
								new AnnotatedType(parameterizedType.getActualTypeArguments()[0]).resolveAsRef(true));
						if (resolvedSchema.schema != null) {
							schemaN = resolvedSchema.schema;
							@SuppressWarnings("rawtypes")
							Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
							if (schemaMap != null) {
								schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
							}
						}
					}
				} else if (Void.class.getName().equals(parameterizedType.getActualTypeArguments()[0].getTypeName())) {
					// if void, no content
					schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
				}
			}

		} else if (returnType instanceof TypeVariable) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
		} else if (Void.class.getName().equals(returnType.getTypeName())) {
			// if void, no content
			schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
		}
		if (schemaN == null) {
			ResolvedSchema resolvedSchema = ModelConverters.getInstance()
					.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
			if (resolvedSchema.schema != null) {
				schemaN = resolvedSchema.schema;
				@SuppressWarnings("rawtypes")
				Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
				if (schemaMap != null) {
					schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
				}
			}
		}

		if (schemaN == null && returnType instanceof Class) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
		}

		if (schemaN != null) {
			mediaType.setSchema(schemaN);
			if (content.size() == 0)
				content.addMediaType(MediaType.ALL_VALUE, mediaType);

			if (ArrayUtils.isNotEmpty(methodProduces)) {
				for (String mediaType2 : methodProduces) {
					content.addMediaType(mediaType2, mediaType);
				}
			}
		}
		apiResponse.setContent(content);

		if (StringUtils.isBlank(apiResponse.getDescription())) {
			apiResponse.setDescription(DEFAULT_DESCRIPTION);
		}
		String httpCode = Utils.evaluateResponseStatus(handlerMethod.getMethod(), handlerMethod.getBeanType());
		apiResponses.addApiResponse(httpCode, apiResponse);

		// Parsing documentation
		io.swagger.v3.oas.annotations.responses.ApiResponses apiResponsesDoc = ReflectionUtils
				.getAnnotation(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.responses.ApiResponses.class);
		if (apiResponsesDoc != null) {
			io.swagger.v3.oas.annotations.responses.ApiResponse[] responsesArray = apiResponsesDoc.value();
			for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponse2 : responsesArray) {
				ApiResponse apiResponse1 = apiResponses.get(apiResponse2.responseCode());
				if (apiResponse1 == null) {
					apiResponse1 = new ApiResponse();
					apiResponse1.setDescription(apiResponse2.description());
					mediaType = new io.swagger.v3.oas.models.media.MediaType();
					io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponse2.content();
					Content contentElt = new Content();
					for (int i = 0; i < contentdoc.length; i++) {
						io.swagger.v3.oas.models.media.MediaType mediaTypeEl = new io.swagger.v3.oas.models.media.MediaType();
						ArraySchema array = AnnotationsUtils.getArraySchema(contentdoc[i].array(), components, null)
								.orElse(null);
						if (array != null)
							mediaTypeEl.schema(array);
						else
							mediaTypeEl.schema(AnnotationsUtils.getSchema(contentdoc[i], components, null).get());
						contentElt.addMediaType(contentdoc[i].mediaType(), mediaTypeEl);
					}
					apiResponse1.content(contentElt);
					apiResponses.addApiResponse(apiResponse2.responseCode(), apiResponse1);
				} // existing object
				else {
					if (StringUtils.isNotBlank(apiResponse2.description())) {
						apiResponse1.setDescription(apiResponse2.description());
					}
					if (apiResponse2.content().length > 0) {
						mediaType = new io.swagger.v3.oas.models.media.MediaType();
						io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponse2.content();
						Content contentElt = null;
						if (apiResponse1.getContent() != null) {
							contentElt = apiResponse1.getContent();
						} else {
							contentElt = new Content();
						}

						for (int i = 0; i < contentdoc.length; i++) {
							io.swagger.v3.oas.models.media.MediaType mediaTypeEl = new io.swagger.v3.oas.models.media.MediaType();
							Optional<Schema> schemaFromAnnotation = AnnotationsUtils
									.getSchemaFromAnnotation(contentdoc[i].schema(), null);
							schemaFromAnnotation.ifPresent(mediaTypeEl::setSchema);
							if (!schemaFromAnnotation.isPresent()) {
								Optional<ArraySchema> arraySchema = AnnotationsUtils
										.getArraySchema(contentdoc[i].array(), null);
								arraySchema.ifPresent(mediaTypeEl::setSchema);
							}
							mediaTypeEl.schema(AnnotationsUtils.getSchema(contentdoc[i], components, null).get());
							contentElt.addMediaType(contentdoc[i].mediaType(), mediaTypeEl);
						}
						apiResponse1.content(contentElt);
						apiResponses.addApiResponse(apiResponse2.responseCode(), apiResponse1);
					}
				}
			}
		}

		return apiResponses;
	}

	@SuppressWarnings("rawtypes")
	public void build(Components components, Map<String, Object> findControllerAdvice) {
		genericMapResponse = new HashMap<String, ApiResponse>();
		// ControllerAdvice
		List<Method> methods = new ArrayList<Method>();
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

		String description = DEFAULT_DESCRIPTION;
		// for each one build ApiResponse and add it to existing responses
		for (Method method : methods) {
			ApiResponse apiResponse = new ApiResponse();
			Content content = new Content();
			io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();

			RequestMapping reqMappringMethod = ReflectionUtils.getAnnotation(method, RequestMapping.class);

			String[] methodProduces = null;
			if (reqMappringMethod != null) {
				methodProduces = reqMappringMethod.produces();
			}

			// TODO JSON VIEW
			Schema<?> schemaN = null;
			Type returnType = method.getGenericReturnType();
			if (returnType instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) returnType;
				if (ResponseEntity.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
					if (parameterizedType.getActualTypeArguments()[0] instanceof Class && !Void.class.getName()
							.equals(parameterizedType.getActualTypeArguments()[0].getTypeName())) {
						schemaN = AnnotationsUtils.resolveSchemaFromType(
								(Class<?>) parameterizedType.getActualTypeArguments()[0], null, null);
						if (schemaN.getType() == null) {
							ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(
									new AnnotatedType(parameterizedType.getActualTypeArguments()[0])
											.resolveAsRef(true));
							if (resolvedSchema.schema != null) {
								schemaN = resolvedSchema.schema;
								@SuppressWarnings("rawtypes")
								Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
								if (schemaMap != null) {
									schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
								}
							}
						}
					} else if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType && !Void.class
							.getName().equals(parameterizedType.getActualTypeArguments()[0].getTypeName())) {
						parameterizedType = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
						schemaN = AnnotationsUtils.resolveSchemaFromType(
								(Class<?>) parameterizedType.getActualTypeArguments()[0], null, null);
						if (schemaN.getType() == null) {
							ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(
									new AnnotatedType(parameterizedType.getActualTypeArguments()[0])
											.resolveAsRef(true));
							if (resolvedSchema.schema != null) {
								schemaN = resolvedSchema.schema;
								@SuppressWarnings("rawtypes")
								Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
								if (schemaMap != null) {
									schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
								}
							}
						}
					} else if (Void.class.getName()
							.equals(parameterizedType.getActualTypeArguments()[0].getTypeName())) {
						// if void, no content
						schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
					}
				}

			} else if (returnType instanceof TypeVariable) {
				schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
			} else if (Void.class.getName().equals(returnType.getTypeName())) {
				// if void, no content
				schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
			}
			if (schemaN == null) {
				ResolvedSchema resolvedSchema = ModelConverters.getInstance()
						.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
				if (resolvedSchema.schema != null) {
					schemaN = resolvedSchema.schema;
					@SuppressWarnings("rawtypes")
					Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
					if (schemaMap != null) {
						schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
					}
				}
			}

			if (schemaN == null && returnType instanceof Class) {
				schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
			}

			if (schemaN != null) {
				mediaType.setSchema(schemaN);
				if (content.size() == 0)
					content.addMediaType(MediaType.ALL_VALUE, mediaType);

				if (ArrayUtils.isNotEmpty(methodProduces)) {
					for (String mediaType2 : methodProduces) {
						content.addMediaType(mediaType2, mediaType);
					}
				}
			}
			apiResponse.setContent(content);

			if (StringUtils.isBlank(apiResponse.getDescription())) {
				apiResponse.setDescription(DEFAULT_DESCRIPTION);
			}
			String httpCode = Utils.evaluateResponseStatus(method, method.getClass());
			genericMapResponse.put(httpCode, apiResponse);

			// Parsing documentation
			io.swagger.v3.oas.annotations.responses.ApiResponses apiResponsesDoc = ReflectionUtils.getAnnotation(method,
					io.swagger.v3.oas.annotations.responses.ApiResponses.class);
			if (apiResponsesDoc != null) {
				io.swagger.v3.oas.annotations.responses.ApiResponse[] responsesArray = apiResponsesDoc.value();
				for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponse2 : responsesArray) {
					ApiResponse apiResponse1 = genericMapResponse.get(apiResponse2.responseCode());
					if (apiResponse1 == null) {
						apiResponse1 = new ApiResponse();
						apiResponse1.setDescription(apiResponse2.description());
						mediaType = new io.swagger.v3.oas.models.media.MediaType();
						io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponse2.content();
						Content contentElt = new Content();
						for (int i = 0; i < contentdoc.length; i++) {
							io.swagger.v3.oas.models.media.MediaType mediaTypeEl = new io.swagger.v3.oas.models.media.MediaType();
							ArraySchema array = AnnotationsUtils.getArraySchema(contentdoc[i].array(), components, null)
									.orElse(null);
							if (array != null)
								mediaTypeEl.schema(array);
							else
								mediaTypeEl.schema(AnnotationsUtils.getSchema(contentdoc[i], components, null).get());
							contentElt.addMediaType(contentdoc[i].mediaType(), mediaTypeEl);
						}
						apiResponse1.content(contentElt);
						genericMapResponse.put(apiResponse2.responseCode(), apiResponse1);
					} // existing object
					else {
						if (StringUtils.isNotBlank(apiResponse2.description())) {
							apiResponse1.setDescription(apiResponse2.description());
						}
						if (apiResponse2.content().length > 0) {
							mediaType = new io.swagger.v3.oas.models.media.MediaType();
							io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponse2.content();
							Content contentElt = null;
							if (apiResponse1.getContent() != null) {
								contentElt = apiResponse1.getContent();
							} else {
								contentElt = new Content();
							}

							for (int i = 0; i < contentdoc.length; i++) {
								io.swagger.v3.oas.models.media.MediaType mediaTypeEl = new io.swagger.v3.oas.models.media.MediaType();
								Optional<Schema> schemaFromAnnotation = AnnotationsUtils
										.getSchemaFromAnnotation(contentdoc[i].schema(), null);
								schemaFromAnnotation.ifPresent(mediaTypeEl::setSchema);
								if (!schemaFromAnnotation.isPresent()) {
									Optional<ArraySchema> arraySchema = AnnotationsUtils
											.getArraySchema(contentdoc[i].array(), null);
									arraySchema.ifPresent(mediaTypeEl::setSchema);
								}
								mediaTypeEl.schema(AnnotationsUtils.getSchema(contentdoc[i], components, null).get());
								contentElt.addMediaType(contentdoc[i].mediaType(), mediaTypeEl);
							}
							apiResponse1.content(contentElt);
							genericMapResponse.put(apiResponse2.responseCode(), apiResponse1);
						}
					}
				}
			}
		}
	}

}
