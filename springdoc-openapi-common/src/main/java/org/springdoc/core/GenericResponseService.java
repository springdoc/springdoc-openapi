/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2023 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;

import static java.util.Arrays.asList;
import static org.springdoc.core.Constants.DEFAULT_DESCRIPTION;
import static org.springdoc.core.SpringDocAnnotationsUtils.extractSchema;
import static org.springdoc.core.SpringDocAnnotationsUtils.getContent;
import static org.springdoc.core.SpringDocAnnotationsUtils.mergeSchema;
import static org.springdoc.core.converters.ConverterUtils.isResponseTypeWrapper;

/**
 * The type Generic response builder.
 *
 * @author bnasslahsen
 */
public class GenericResponseService {
	/**
	 * This extension name is used to temporary store
	 * the exception classes.
	 */
	private static final String EXTENSION_EXCEPTION_CLASSES = "x-exception-class";

	/**
	 * The Response entity exception handler class.
	 */
	private static Class<?> responseEntityExceptionHandlerClass;

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
	 * The Controller infos.
	 */
	private final List<ControllerAdviceInfo> localExceptionHandlers = new ArrayList<>();

	/**
	 * The Controller advice infos.
	 */
	private final List<ControllerAdviceInfo> controllerAdviceInfos = new ArrayList<>();

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericResponseService.class);

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
	 * Sets response entity exception handler class.
	 *
	 * @param responseEntityExceptionHandlerClass the response entity exception handler class
	 */
	public static void setResponseEntityExceptionHandlerClass(Class<?> responseEntityExceptionHandlerClass) {
		GenericResponseService.responseEntityExceptionHandlerClass = responseEntityExceptionHandlerClass;
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
		Map<String, ApiResponse> genericMapResponse = getGenericMapResponse(handlerMethod.getBeanType());
		if (springDocConfigProperties.isOverrideWithGenericResponse()) {
			genericMapResponse = filterAndEnrichGenericMapResponseByDeclarations(handlerMethod, genericMapResponse);
		}
		ApiResponses apiResponses = methodAttributes.calculateGenericMapResponse(genericMapResponse);
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
	 * Filters the generic API responses by the declared exceptions.
	 * If Javadoc comment found for the declaration than it overrides the default description.
	 *
	 * @param handlerMethod the method which can have exception declarations
	 * @param genericMapResponse the default generic API responses
	 * @return the filtered and enriched responses
	 */
	private Map<String, ApiResponse> filterAndEnrichGenericMapResponseByDeclarations(HandlerMethod handlerMethod, Map<String, ApiResponse> genericMapResponse) {
		if (operationService.getJavadocProvider() != null) {
			JavadocProvider javadocProvider = operationService.getJavadocProvider();
			for (Map.Entry<String, ApiResponse> genericResponse : genericMapResponse.entrySet()) {
				Map<String, Object> extensions = genericResponse.getValue().getExtensions();
				Collection<String> genericExceptions = (Collection<String>) extensions.get(EXTENSION_EXCEPTION_CLASSES);
				for (Class<?> declaredException : handlerMethod.getMethod().getExceptionTypes()) {
					if (genericExceptions.contains(declaredException.getName())) {
						Map<String, String> javadocThrows = javadocProvider.getMethodJavadocThrows(handlerMethod.getMethod());
						String description = javadocThrows.get(declaredException.getName());
						if (description == null)
							description = javadocThrows.get(declaredException.getSimpleName());
						if (description != null && !description.trim().isEmpty()) {
							genericResponse.getValue().setDescription(description);
						}
					}
				}
			}
		}
		return genericMapResponse;
	}

	/**
	 * Build generic response.
	 *
	 * @param components the components
	 * @param findControllerAdvice the find controller advice
	 * @param locale the locale
	 */
	public void buildGenericResponse(Components components, Map<String, Object> findControllerAdvice, Locale locale) {
		// ControllerAdvice
		for (Map.Entry<String, Object> entry : findControllerAdvice.entrySet()) {
			List<Method> methods = new ArrayList<>();
			Object controllerAdvice = entry.getValue();
			// get all methods with annotation @ExceptionHandler
			Class<?> objClz = controllerAdvice.getClass();
			if (org.springframework.aop.support.AopUtils.isAopProxy(controllerAdvice))
				objClz = org.springframework.aop.support.AopUtils.getTargetClass(controllerAdvice);
			ControllerAdviceInfo controllerAdviceInfo = new ControllerAdviceInfo(controllerAdvice);
			Arrays.stream(ReflectionUtils.getAllDeclaredMethods(objClz))
					.filter(m -> m.isAnnotationPresent(ExceptionHandler.class)
							|| isResponseEntityExceptionHandlerMethod(m)
					).forEach(methods::add);
			// for each one build ApiResponse and add it to existing responses
			for (Method method : methods) {
				if (!operationService.isHidden(method)) {
					RequestMapping reqMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
					String[] methodProduces = { springDocConfigProperties.getDefaultProducesMediaType() };
					if (reqMappingMethod != null)
						methodProduces = reqMappingMethod.produces();
					Map<String, ApiResponse> controllerAdviceInfoApiResponseMap = controllerAdviceInfo.getApiResponseMap();
					MethodParameter methodParameter = new MethodParameter(method, -1);
					ApiResponses apiResponsesOp = new ApiResponses();
					MethodAttributes methodAttributes = new MethodAttributes(methodProduces, springDocConfigProperties.getDefaultConsumesMediaType(),
							springDocConfigProperties.getDefaultProducesMediaType(), controllerAdviceInfoApiResponseMap, locale);
					//calculate JsonView Annotation
					methodAttributes.setJsonViewAnnotation(AnnotatedElementUtils.findMergedAnnotation(method, JsonView.class));
					//use the javadoc return if present
					if (operationService.getJavadocProvider() != null) {
						JavadocProvider javadocProvider = operationService.getJavadocProvider();
						methodAttributes.setJavadocReturn(javadocProvider.getMethodJavadocReturn(methodParameter.getMethod()));
					}
					Map<String, ApiResponse> apiResponses = computeResponseFromDoc(components, methodParameter, apiResponsesOp, methodAttributes);
					buildGenericApiResponses(components, methodParameter, apiResponsesOp, methodAttributes);
					apiResponses.forEach(controllerAdviceInfoApiResponseMap::put);
				}
			}
			synchronized (this) {
				if (AnnotatedElementUtils.hasAnnotation(objClz, ControllerAdvice.class)) {
					controllerAdviceInfos.add(controllerAdviceInfo);
				}
				else {
					localExceptionHandlers.add(controllerAdviceInfo);
				}
			}
		}
	}

	/**
	 * Is response entity exception handler method boolean.
	 *
	 * @param m the m
	 * @return the boolean
	 */
	private boolean isResponseEntityExceptionHandlerMethod(Method m) {
		if (AnnotatedElementUtils.hasAnnotation(m.getDeclaringClass(), ControllerAdvice.class))
			return responseEntityExceptionHandlerClass != null && (responseEntityExceptionHandlerClass.isAssignableFrom(m.getDeclaringClass()) && ReflectionUtils.findMethod(responseEntityExceptionHandlerClass, m.getName(), m.getParameterTypes()) != null);
		return false;
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
				apiResponse.setDescription(propertyResolverUtils.resolve(apiResponseAnnotations.description(), methodAttributes.getLocale()));
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
			if (Objects.nonNull(httpCode)) {
				ApiResponse apiResponse = methodAttributes.getGenericMapResponse().containsKey(httpCode) ? methodAttributes.getGenericMapResponse().get(httpCode)
						: new ApiResponse();
				buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, apiResponse, true);
			}
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

			if (AnnotatedElementUtils.hasAnnotation(methodParameter.getMethod(), ResponseStatus.class)) {
				// Handles the case with @ResponseStatus, if the specified response is not already handled explicitly
				String httpCode = evaluateResponseStatus(methodParameter.getMethod(), Objects.requireNonNull(methodParameter.getMethod()).getClass(), false);
				if (Objects.nonNull(httpCode) && !apiResponsesOp.containsKey(httpCode) && !apiResponsesOp.containsKey(ApiResponses.DEFAULT)) {
					buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, new ApiResponse(), false);
				}
			}

		}
		else {
			String httpCode = evaluateResponseStatus(methodParameter.getMethod(), Objects.requireNonNull(methodParameter.getMethod()).getClass(), false);
			if (Objects.nonNull(httpCode))
				buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, new ApiResponse(), false);
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
				// use javadoc
				if (!StringUtils.isBlank(methodAttributes.getJavadocReturn()))
					apiResponse.setDescription(methodAttributes.getJavadocReturn());
				else
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
		if (springDocConfigProperties.isOverrideWithGenericResponse()
				&& methodParameter.getExecutable().isAnnotationPresent(ExceptionHandler.class)) {
			// ExceptionHandler's exception class resolution is non-trivial
			// more info on its javadoc
			ExceptionHandler exceptionHandler = methodParameter.getExecutable().getAnnotation(ExceptionHandler.class);
			Set<Class<?>> exceptions = new HashSet<>();
			if (exceptionHandler.value().length == 0) {
				for (Parameter parameter : methodParameter.getExecutable().getParameters()) {
					if (Throwable.class.isAssignableFrom(parameter.getType())) {
						exceptions.add(parameter.getType());
					}
				}
			}
			else {
				exceptions.addAll(asList(exceptionHandler.value()));
			}
			apiResponse.addExtension(EXTENSION_EXCEPTION_CLASSES, exceptions);
		}
		apiResponsesOp.addApiResponse(httpCode, apiResponse);
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
		if (Void.TYPE.equals(returnType) || Void.class.equals(returnType))
			result = true;
		else if (returnType instanceof ParameterizedType) {
			Type[] types = ((ParameterizedType) returnType).getActualTypeArguments();
			if (types != null && isResponseTypeWrapper(ResolvableType.forType(returnType).getRawClass()))
				result = isVoid(types[0]);
		}
		return result;
	}

	/**
	 * Gets generic map response.
	 *
	 * @param beanType the bean type
	 * @return the generic map response
	 */
	private synchronized Map<String, ApiResponse> getGenericMapResponse(Class<?> beanType) {
		List<ControllerAdviceInfo> controllerAdviceInfosInThisBean = localExceptionHandlers.stream()
				.filter(controllerInfo -> {
					Class<?> objClz = controllerInfo.getControllerAdvice().getClass();
					if (org.springframework.aop.support.AopUtils.isAopProxy(controllerInfo.getControllerAdvice()))
						objClz = org.springframework.aop.support.AopUtils.getTargetClass(controllerInfo.getControllerAdvice());
					return beanType.equals(objClz);
				})
				.collect(Collectors.toList());

		Map<String, ApiResponse> genericApiResponseMap = controllerAdviceInfosInThisBean.stream()
				.map(ControllerAdviceInfo::getApiResponseMap)
				.collect(LinkedHashMap::new, Map::putAll, Map::putAll);

		List<ControllerAdviceInfo> controllerAdviceInfosNotInThisBean = controllerAdviceInfos.stream()
				.filter(controllerAdviceInfo ->
						new ControllerAdviceBean(controllerAdviceInfo.getControllerAdvice()).isApplicableToBeanType(beanType))
				.collect(Collectors.toList());

		for (ControllerAdviceInfo controllerAdviceInfo : controllerAdviceInfosNotInThisBean) {
			controllerAdviceInfo.getApiResponseMap().forEach((key, apiResponse) -> {
				if (!genericApiResponseMap.containsKey(key))
					genericApiResponseMap.put(key, apiResponse);
			});
		}

		LinkedHashMap<String, ApiResponse> genericApiResponsesClone;
		try {
			ObjectMapper objectMapper = ObjectMapperProvider.createJson(springDocConfigProperties);
			genericApiResponsesClone = objectMapper.readValue(objectMapper.writeValueAsString(genericApiResponseMap), ApiResponses.class);
			return genericApiResponsesClone;
		}
		catch (JsonProcessingException e) {
			LOGGER.warn("Json Processing Exception occurred: {}", e.getMessage());
			return genericApiResponseMap;
		}
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
		if (method != null) {
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
