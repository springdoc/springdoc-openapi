/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.Constants.DEFAULT_DESCRIPTION;
import static org.springdoc.core.SpringDocAnnotationsUtils.extractSchema;
import static org.springdoc.core.SpringDocAnnotationsUtils.getContent;
import static org.springdoc.core.SpringDocAnnotationsUtils.mergeSchema;
import static org.springdoc.core.converters.ConverterUtils.isResponseTypeWrapper;

/**
 * The type Generic response builder.
 * @author bnasslahsen
 */
public class GenericResponseService {

	/**
	 * The Operation builder.
	 */
	private final OperationService operationService;

	/**
	 * The Return type parsers.
	 */
	private final List<ReturnTypeParser> returnTypeParsers;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Property resolver utils.
	 */
	private final PropertyResolverUtils propertyResolverUtils;

	/**
	 * The Controller advice infos.
	 */
	private List<ControllerAdviceInfo> controllerAdviceInfos = new ArrayList<>();

	/**
	 * Instantiates a new Generic response builder.
	 *
	 * @param operationService the operation builder
	 * @param returnTypeParsers the return type parsers
	 * @param springDocConfigProperties the spring doc config properties
	 * @param propertyResolverUtils the property resolver utils
	 */
	public GenericResponseService(OperationService operationService, List<ReturnTypeParser> returnTypeParsers,
			SpringDocConfigProperties springDocConfigProperties,
			PropertyResolverUtils propertyResolverUtils) {
		super();
		this.operationService = operationService;
		this.returnTypeParsers = returnTypeParsers;
		this.springDocConfigProperties = springDocConfigProperties;
		this.propertyResolverUtils = propertyResolverUtils;
	}

	/**
	 * Build api responses.
	 *
	 * @param components the components
	 * @param handlerMethod the handler method
	 * @param operation the operation
	 * @param methodAttributes the method attributes
	 * @return the api responses
	 */
	public ApiResponses build(Components components, HandlerMethod handlerMethod, Operation operation,
			MethodAttributes methodAttributes) {
		ApiResponses apiResponses = methodAttributes.calculateGenericMapResponse(getGenericMapResponse(handlerMethod.getBeanType()));
		//Then use the apiResponses from documentation
		ApiResponses apiResponsesFromDoc = operation.getResponses();
		if (!CollectionUtils.isEmpty(apiResponsesFromDoc))
			apiResponsesFromDoc.forEach(apiResponses::addApiResponse);
		// for each one build ApiResponse and add it to existing responses
		// Fill api Responses
		computeResponseFromDoc(components, handlerMethod.getReturnType(), apiResponses, methodAttributes);
		buildApiResponses(components, handlerMethod.getReturnType(), apiResponses, methodAttributes);
		return apiResponses;
	}

	/**
	 * Build generic response.
	 *
	 * @param components the components
	 * @param findControllerAdvice the find controller advice
	 */
	public void buildGenericResponse(Components components, Map<String, Object> findControllerAdvice) {
		// ControllerAdvice
		for (Map.Entry<String, Object> entry : findControllerAdvice.entrySet()) {
			List<Method> methods = new ArrayList<>();
			Object controllerAdvice = entry.getValue();
			// get all methods with annotation @ExceptionHandler
			Class<?> objClz = controllerAdvice.getClass();
			if (org.springframework.aop.support.AopUtils.isAopProxy(controllerAdvice))
				objClz = org.springframework.aop.support.AopUtils.getTargetClass(controllerAdvice);
			ControllerAdviceInfo controllerAdviceInfo = new ControllerAdviceInfo(controllerAdvice);
			Arrays.stream(objClz.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(ExceptionHandler.class)).forEach(methods::add);
			// for each one build ApiResponse and add it to existing responses
			for (Method method : methods) {
				if (!operationService.isHidden(method)) {
					RequestMapping reqMappringMethod = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
					String[] methodProduces = { springDocConfigProperties.getDefaultProducesMediaType() };
					if (reqMappringMethod != null)
						methodProduces = reqMappringMethod.produces();
					Map<String, ApiResponse> controllerAdviceInfoApiResponseMap = controllerAdviceInfo.getApiResponseMap();
					MethodParameter methodParameter = new MethodParameter(method, -1);
					ApiResponses apiResponsesOp = new ApiResponses();
					MethodAttributes methodAttributes = new MethodAttributes(methodProduces, springDocConfigProperties.getDefaultConsumesMediaType(),
							springDocConfigProperties.getDefaultProducesMediaType(), controllerAdviceInfoApiResponseMap);
					Map<String, ApiResponse> apiResponses = computeResponseFromDoc(components, methodParameter, apiResponsesOp, methodAttributes);
					buildGenericApiResponses(components, methodParameter, apiResponsesOp, methodAttributes);
					apiResponses.forEach(controllerAdviceInfoApiResponseMap::put);
				}
			}
			controllerAdviceInfos.add(controllerAdviceInfo);
		}
	}

	/**
	 * Compute response from doc map.
	 *
	 * @param components the components
	 * @param methodParameter the method parameter
	 * @param apiResponsesOp the api responses op
	 * @param methodAttributes the method attributes
	 * @return the map
	 */
	private Map<String, ApiResponse> computeResponseFromDoc(Components components, MethodParameter methodParameter, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes) {
		// Parsing documentation, if present
		Set<io.swagger.v3.oas.annotations.responses.ApiResponse> responsesArray = getApiResponses(Objects.requireNonNull(methodParameter.getMethod()));
		if (!responsesArray.isEmpty()) {
			methodAttributes.setWithApiResponseDoc(true);
			for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotations : responsesArray) {
				String httpCode = apiResponseAnnotations.responseCode();
				ApiResponse apiResponse = new ApiResponse();
				if (StringUtils.isNotBlank(apiResponseAnnotations.ref())) {
					apiResponse.$ref(apiResponseAnnotations.ref());
					apiResponsesOp.addApiResponse(apiResponseAnnotations.responseCode(), apiResponse);
					continue;
				}
				apiResponse.setDescription(propertyResolverUtils.resolve(apiResponseAnnotations.description()));
				buildContentFromDoc(components, apiResponsesOp, methodAttributes, apiResponseAnnotations, apiResponse);
				Map<String, Object> extensions = AnnotationsUtils.getExtensions(apiResponseAnnotations.extensions());
				if (!CollectionUtils.isEmpty(extensions))
					apiResponse.extensions(extensions);
				AnnotationsUtils.getHeaders(apiResponseAnnotations.headers(), methodAttributes.getJsonViewAnnotation())
						.ifPresent(apiResponse::headers);
				apiResponsesOp.addApiResponse(httpCode, apiResponse);
			}
		}
		return apiResponsesOp;
	}

	/**
	 * Build content from doc.
	 *
	 * @param components the components
	 * @param apiResponsesOp the api responses op
	 * @param methodAttributes the method attributes
	 * @param apiResponseAnnotations the api response annotations
	 * @param apiResponse the api response
	 */
	public static void buildContentFromDoc(Components components, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes,
			io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotations,
			ApiResponse apiResponse) {

		io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponseAnnotations.content();
		Optional<Content> optionalContent = getContent(contentdoc, new String[0],
				methodAttributes.getMethodProduces(), null, components, methodAttributes.getJsonViewAnnotation());
		if (apiResponsesOp.containsKey(apiResponseAnnotations.responseCode())) {
			// Merge with the existing content
			Content existingContent = apiResponsesOp.get(apiResponseAnnotations.responseCode()).getContent();
			if (optionalContent.isPresent()) {
				Content newContent = optionalContent.get();
				if (methodAttributes.isMethodOverloaded() && existingContent != null) {
					Arrays.stream(methodAttributes.getMethodProduces()).filter(mediaTypeStr -> (newContent.get(mediaTypeStr) != null)).forEach(mediaTypeStr -> {
						if (newContent.get(mediaTypeStr).getSchema() != null)
							mergeSchema(existingContent, newContent.get(mediaTypeStr).getSchema(), mediaTypeStr);
					});
					apiResponse.content(existingContent);
				}
				else
					apiResponse.content(newContent);
			}
			else {
				apiResponse.content(existingContent);
			}
		}
		else {
			optionalContent.ifPresent(apiResponse::content);
		}
	}

	/**
	 * Build generic api responses.
	 *
	 * @param components the components
	 * @param methodParameter the method parameter
	 * @param apiResponsesOp the api responses op
	 * @param methodAttributes the method attributes
	 */
	private void buildGenericApiResponses(Components components, MethodParameter methodParameter, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes) {
		if (!CollectionUtils.isEmpty(apiResponsesOp)) {
			// API Responses at operation and @ApiResponse annotation
			for (Map.Entry<String, ApiResponse> entry : apiResponsesOp.entrySet()) {
				String httpCode = entry.getKey();
				ApiResponse apiResponse = entry.getValue();
				buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, apiResponse, true);
			}
		}
		else {
			// Use response parameters with no description filled - No documentation
			// available
			String httpCode = evaluateResponseStatus(methodParameter.getMethod(), Objects.requireNonNull(methodParameter.getMethod()).getClass(), true);
			ApiResponse apiResponse = methodAttributes.getGenericMapResponse().containsKey(httpCode) ? methodAttributes.getGenericMapResponse().get(httpCode)
					: new ApiResponse();
			if (httpCode != null)
				buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, apiResponse, true);
		}
	}

	/**
	 * Build api responses.
	 *
	 * @param components the components
	 * @param methodParameter the method parameter
	 * @param apiResponsesOp the api responses op
	 * @param methodAttributes the method attributes
	 */
	private void buildApiResponses(Components components, MethodParameter methodParameter, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes) {
		Map<String, ApiResponse> genericMapResponse = methodAttributes.getGenericMapResponse();
		if (!CollectionUtils.isEmpty(apiResponsesOp) && apiResponsesOp.size() > genericMapResponse.size()) {
			// API Responses at operation and @ApiResponse annotation
			for (Map.Entry<String, ApiResponse> entry : apiResponsesOp.entrySet()) {
				String httpCode = entry.getKey();
				boolean methodAttributesCondition = !methodAttributes.isMethodOverloaded() || (methodAttributes.isMethodOverloaded() && isValidHttpCode(httpCode, methodParameter));
				if (!genericMapResponse.containsKey(httpCode) && methodAttributesCondition) {
					ApiResponse apiResponse = entry.getValue();
					buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, apiResponse, false);
				}
			}
		}
		else {
			// Use response parameters with no description filled - No documentation
			// available
			String httpCode = evaluateResponseStatus(methodParameter.getMethod(), Objects.requireNonNull(methodParameter.getMethod()).getClass(), false);
			ApiResponse apiResponse = new ApiResponse();
			if (httpCode != null)
				buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, apiResponse, false);
		}
	}

	/**
	 * Gets api responses.
	 *
	 * @param method the method
	 * @return the api responses
	 */
	public Set<io.swagger.v3.oas.annotations.responses.ApiResponse> getApiResponses(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();

		Set<io.swagger.v3.oas.annotations.responses.ApiResponses> apiResponsesDoc = AnnotatedElementUtils
				.findAllMergedAnnotations(method, io.swagger.v3.oas.annotations.responses.ApiResponses.class);
		Set<io.swagger.v3.oas.annotations.responses.ApiResponse> responses = apiResponsesDoc.stream()
				.flatMap(x -> Stream.of(x.value())).collect(Collectors.toSet());

		Set<io.swagger.v3.oas.annotations.responses.ApiResponses> apiResponsesDocDeclaringClass = AnnotatedElementUtils
				.findAllMergedAnnotations(declaringClass, io.swagger.v3.oas.annotations.responses.ApiResponses.class);
		responses.addAll(
				apiResponsesDocDeclaringClass.stream().flatMap(x -> Stream.of(x.value())).collect(Collectors.toSet()));

		Set<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseDoc = AnnotatedElementUtils
				.findMergedRepeatableAnnotations(method, io.swagger.v3.oas.annotations.responses.ApiResponse.class);
		responses.addAll(apiResponseDoc);

		Set<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseDocDeclaringClass = AnnotatedElementUtils
				.findMergedRepeatableAnnotations(declaringClass,
						io.swagger.v3.oas.annotations.responses.ApiResponse.class);
		responses.addAll(apiResponseDocDeclaringClass);

		return responses;
	}

	/**
	 * Build content content.
	 *
	 * @param components the components
	 * @param methodParameter the method parameter
	 * @param methodProduces the method produces
	 * @param jsonView the json view
	 * @return the content
	 */
	private Content buildContent(Components components, MethodParameter methodParameter, String[] methodProduces, JsonView jsonView) {
		Type returnType = getReturnType(methodParameter);
		return buildContent(components, methodParameter.getParameterAnnotations(), methodProduces, jsonView, returnType);
	}

	/**
	 * Build content content.
	 *
	 * @param components the components
	 * @param annotations the annotations
	 * @param methodProduces the method produces
	 * @param jsonView the json view
	 * @param returnType the return type
	 * @return the content
	 */
	public Content buildContent(Components components, Annotation[] annotations, String[] methodProduces, JsonView jsonView, Type returnType) {
		Content content = new Content();
		// if void, no content
		if (isVoid(returnType))
			return null;
		if (ArrayUtils.isNotEmpty(methodProduces)) {
			Schema<?> schemaN = calculateSchema(components, returnType, jsonView, annotations);
			if (schemaN != null) {
				io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
				mediaType.setSchema(schemaN);
				// Fill the content
				setContent(methodProduces, content, mediaType);
			}
		}
		return content;
	}

	/**
	 * Gets return type.
	 *
	 * @param methodParameter the method parameter
	 * @return the return type
	 */
	private Type getReturnType(MethodParameter methodParameter) {
		Type returnType = Object.class;
		for (ReturnTypeParser returnTypeParser : returnTypeParsers) {
			if (returnType.getTypeName().equals(Object.class.getTypeName())) {
				returnType = returnTypeParser.getReturnType(methodParameter);
			}
			else
				break;
		}

		return returnType;
	}

	/**
	 * Calculate schema schema.
	 *
	 * @param components the components
	 * @param returnType the return type
	 * @param jsonView the json view
	 * @param annotations the annotations
	 * @return the schema
	 */
	private Schema<?> calculateSchema(Components components, Type returnType, JsonView jsonView, Annotation[] annotations) {
		if (!isVoid(returnType) && !SpringDocAnnotationsUtils.isAnnotationToIgnore(returnType))
			return extractSchema(components, returnType, jsonView, annotations);
		return null;
	}

	/**
	 * Sets content.
	 *
	 * @param methodProduces the method produces
	 * @param content the content
	 * @param mediaType the media type
	 */
	private void setContent(String[] methodProduces, Content content,
			io.swagger.v3.oas.models.media.MediaType mediaType) {
		Arrays.stream(methodProduces).forEach(mediaTypeStr -> content.addMediaType(mediaTypeStr, mediaType));
	}

	/**
	 * Build api responses.
	 *
	 * @param components the components
	 * @param methodParameter the method parameter
	 * @param apiResponsesOp the api responses op
	 * @param methodAttributes the method attributes
	 * @param httpCode the http code
	 * @param apiResponse the api response
	 * @param isGeneric the is generic
	 */
	private void buildApiResponses(Components components, MethodParameter methodParameter, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes, String httpCode, ApiResponse apiResponse, boolean isGeneric) {
		// No documentation
		if (StringUtils.isBlank(apiResponse.get$ref())) {
			if (apiResponse.getContent() == null) {
				Content content = buildContent(components, methodParameter, methodAttributes.getMethodProduces(),
						methodAttributes.getJsonViewAnnotation());
				apiResponse.setContent(content);
			}
			else if (CollectionUtils.isEmpty(apiResponse.getContent()))
				apiResponse.setContent(null);
			if (StringUtils.isBlank(apiResponse.getDescription())) {
				setDescription(httpCode, apiResponse);
			}
		}
		if (apiResponse.getContent() != null
				&& ((isGeneric || methodAttributes.isMethodOverloaded()) && methodAttributes.isNoApiResponseDoc())) {
			// Merge with existing schema
			Content existingContent = apiResponse.getContent();
			Type type = ReturnTypeParser.getType(methodParameter);
			Schema<?> schemaN = calculateSchema(components, type,
					methodAttributes.getJsonViewAnnotation(), methodParameter.getParameterAnnotations());
			if (schemaN != null && ArrayUtils.isNotEmpty(methodAttributes.getMethodProduces()))
				Arrays.stream(methodAttributes.getMethodProduces()).forEach(mediaTypeStr -> mergeSchema(existingContent, schemaN, mediaTypeStr));
		}
		apiResponsesOp.addApiResponse(httpCode, apiResponse);
	}

	/**
	 * Sets description.
	 *
	 * @param httpCode the http code
	 * @param apiResponse the api response
	 */
	public static void setDescription(String httpCode, ApiResponse apiResponse) {
		try {
			HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(httpCode));
			apiResponse.setDescription(httpStatus.getReasonPhrase());
		}
		catch (IllegalArgumentException e) {
			apiResponse.setDescription(DEFAULT_DESCRIPTION);
		}
	}

	/**
	 * Evaluate response status string.
	 *
	 * @param method the method
	 * @param beanType the bean type
	 * @param isGeneric the is generic
	 * @return the string
	 */
	public String evaluateResponseStatus(Method method, Class<?> beanType, boolean isGeneric) {
		String responseStatus = null;
		ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(method, ResponseStatus.class);
		if (annotation == null && beanType != null)
			annotation = AnnotatedElementUtils.findMergedAnnotation(beanType, ResponseStatus.class);
		if (annotation != null)
			responseStatus = String.valueOf(annotation.code().value());
		if (annotation == null && !isGeneric)
			responseStatus = String.valueOf(HttpStatus.OK.value());
		return responseStatus;
	}

	/**
	 * Is void boolean.
	 *
	 * @param returnType the return type
	 * @return the boolean
	 */
	private boolean isVoid(Type returnType) {
		boolean result = false;
		if (Void.TYPE.equals(returnType))
			result = true;
		else if (returnType instanceof ParameterizedType) {
			Type[] types = ((ParameterizedType) returnType).getActualTypeArguments();
			if (types != null && isResponseTypeWrapper(ResolvableType.forType(returnType).getRawClass()))
				return isVoid(types[0]);
		}
		if (Void.class.equals(returnType))
			result = true;
		return result;
	}

	/**
	 * Gets generic map response.
	 *
	 * @param beanType the bean type
	 * @return the generic map response
	 */
	private Map<String, ApiResponse> getGenericMapResponse(Class<?> beanType) {
		return controllerAdviceInfos.stream()
				.filter(controllerAdviceInfo -> new ControllerAdviceBean(controllerAdviceInfo.getControllerAdvice()).isApplicableToBeanType(beanType))
				.map(ControllerAdviceInfo::getApiResponseMap)
				.collect(LinkedHashMap::new, Map::putAll, Map::putAll);
	}

	/**
	 * Is valid http code boolean.
	 *
	 * @param httpCode the http code
	 * @param methodParameter the method parameter
	 * @return the boolean
	 */
	private boolean isValidHttpCode(String httpCode, MethodParameter methodParameter) {
		boolean result = false;
		final Method method = methodParameter.getMethod();
		if(method!=null){
			Set<io.swagger.v3.oas.annotations.responses.ApiResponse> responseSet = getApiResponses(method);
			if (isHttpCodePresent(httpCode, responseSet))
				result = true;
			else {
				final io.swagger.v3.oas.annotations.Operation apiOperation = AnnotatedElementUtils.findMergedAnnotation(method,
						io.swagger.v3.oas.annotations.Operation.class);
				if (apiOperation != null) {
					responseSet = new HashSet<>(Arrays.asList(apiOperation.responses()));
					if (isHttpCodePresent(httpCode, responseSet))
						result = true;
				}
				else if (httpCode.equals(evaluateResponseStatus(method, method.getClass(), false)))
					result = true;
			}
		}
		return result;
	}

	/**
	 * Is http code present boolean.
	 *
	 * @param httpCode the http code
	 * @param responseSet the response set
	 * @return the boolean
	 */
	private boolean isHttpCodePresent(String httpCode, Set<io.swagger.v3.oas.annotations.responses.ApiResponse> responseSet) {
		return !responseSet.isEmpty() && responseSet.stream().anyMatch(apiResponseAnnotations -> httpCode.equals(apiResponseAnnotations.responseCode()));
	}

}
