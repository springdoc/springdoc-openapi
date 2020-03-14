/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.Constants.DEFAULT_DESCRIPTION;
import static org.springdoc.core.converters.ConverterUtils.isResponseTypeToIgnore;

@SuppressWarnings("rawtypes")
public class GenericResponseBuilder {

	private final Map<String, ApiResponse> genericMapResponse = new LinkedHashMap<>();

	private final OperationBuilder operationBuilder;

	private final List<ReturnTypeParser> returnTypeParsers;

	private final SpringDocConfigProperties springDocConfigProperties;

	GenericResponseBuilder(OperationBuilder operationBuilder, List<ReturnTypeParser> returnTypeParsers, SpringDocConfigProperties springDocConfigProperties) {
		super();
		this.operationBuilder = operationBuilder;
		this.returnTypeParsers = returnTypeParsers;
		this.springDocConfigProperties = springDocConfigProperties;
	}

	public ApiResponses build(Components components, HandlerMethod handlerMethod, Operation operation,
			MethodAttributes methodAttributes) {
		ApiResponses apiResponses = new ApiResponses();
		genericMapResponse.forEach(apiResponses::addApiResponse);
		//Then use the apiResponses from documentation
		ApiResponses apiResponsesFromDoc = operation.getResponses();
		if (!CollectionUtils.isEmpty(apiResponsesFromDoc))
			apiResponsesFromDoc.forEach(apiResponses::addApiResponse);
		// for each one build ApiResponse and add it to existing responses
		// Fill api Responses
		computeResponse(components, handlerMethod.getReturnType(), apiResponses, methodAttributes, false);
		return apiResponses;
	}

	public void buildGenericResponse(Components components, Map<String, Object> findControllerAdvice) {
		// ControllerAdvice
		List<Method> methods = getMethods(findControllerAdvice);
		// for each one build ApiResponse and add it to existing responses
		for (Method method : methods) {
			if (!operationBuilder.isHidden(method)) {
				RequestMapping reqMappringMethod = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
				String[] methodProduces = { springDocConfigProperties.getDefaultProducesMediaType() };
				if (reqMappringMethod != null) {
					methodProduces = reqMappringMethod.produces();
				}
				Map<String, ApiResponse> apiResponses = computeResponse(components, new MethodParameter(method,-1), new ApiResponses(),
						new MethodAttributes(methodProduces, springDocConfigProperties.getDefaultConsumesMediaType(), springDocConfigProperties.getDefaultProducesMediaType()), true);
				apiResponses.forEach(genericMapResponse::put);
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
			Arrays.stream(objClz.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(ExceptionHandler.class)).forEach(methods::add);
		}
		return methods;
	}

	private Map<String, ApiResponse> computeResponse(Components components, MethodParameter methodParameter, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes, boolean isGeneric) {
		// Parsing documentation, if present
		Set<io.swagger.v3.oas.annotations.responses.ApiResponse> responsesArray = getApiResponses(methodParameter.getMethod());
		if (!responsesArray.isEmpty()) {
			methodAttributes.setWithApiResponseDoc(true);
			if (!springDocConfigProperties.isOverrideWithGenericResponse())
				for (String key : genericMapResponse.keySet())
					apiResponsesOp.remove(key);
			for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotations : responsesArray) {
				ApiResponse apiResponse = new ApiResponse();
				if (StringUtils.isNotBlank(apiResponseAnnotations.ref())) {
					apiResponse.$ref(apiResponseAnnotations.ref());
					apiResponsesOp.addApiResponse(apiResponseAnnotations.responseCode(), apiResponse);
					continue;
				}
				apiResponse.setDescription(apiResponseAnnotations.description());
				io.swagger.v3.oas.annotations.media.Content[] contentdoc = apiResponseAnnotations.content();
				buildContentFromDoc(components, apiResponsesOp, methodAttributes,
						apiResponseAnnotations, apiResponse, contentdoc);
				Map<String, Object> extensions = AnnotationsUtils.getExtensions(apiResponseAnnotations.extensions());
				if (!CollectionUtils.isEmpty(extensions))
					apiResponse.extensions(extensions);
				AnnotationsUtils.getHeaders(apiResponseAnnotations.headers(), methodAttributes.getJsonViewAnnotation())
						.ifPresent(apiResponse::headers);
				apiResponsesOp.addApiResponse(apiResponseAnnotations.responseCode(), apiResponse);
			}
		}
		buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, isGeneric);
		return apiResponsesOp;
	}

	private void buildContentFromDoc(Components components, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes,
			io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotations, ApiResponse apiResponse,
			io.swagger.v3.oas.annotations.media.Content[] contentdoc) {
		Optional<Content> optionalContent = SpringDocAnnotationsUtils.getContent(contentdoc, new String[0],
				methodAttributes.getMethodProduces(), null, components, methodAttributes.getJsonViewAnnotation());
		if (apiResponsesOp.containsKey(apiResponseAnnotations.responseCode())) {
			// Merge with the existing content
			Content existingContent = apiResponsesOp.get(apiResponseAnnotations.responseCode()).getContent();
			if (optionalContent.isPresent() && existingContent != null) {
				Content newContent = optionalContent.get();
				if (methodAttributes.isMethodOverloaded()) {
					Arrays.stream(methodAttributes.getMethodProduces()).filter(mediaTypeStr -> (newContent.get(mediaTypeStr) != null)).forEach(mediaTypeStr -> mergeSchema(existingContent, newContent.get(mediaTypeStr).getSchema(), mediaTypeStr));
					apiResponse.content(existingContent);
				}
				else
					apiResponse.content(newContent);
			}
		}
		else {
			optionalContent.ifPresent(apiResponse::content);
		}
	}

	private void buildApiResponses(Components components, MethodParameter methodParameter, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes, boolean isGeneric) {
		if (!CollectionUtils.isEmpty(apiResponsesOp) && (apiResponsesOp.size() != genericMapResponse.size() || isGeneric)) {
			// API Responses at operation and @ApiResponse annotation
			for (Map.Entry<String, ApiResponse> entry : apiResponsesOp.entrySet()) {
				String httpCode = entry.getKey();
				ApiResponse apiResponse = entry.getValue();
				buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, apiResponse,
						isGeneric);
			}
		}
		else {
			// Use response parameters with no description filled - No documentation
			// available
			String httpCode = evaluateResponseStatus(methodParameter.getMethod(), methodParameter.getMethod().getClass(), isGeneric);
			ApiResponse apiResponse = genericMapResponse.containsKey(httpCode) ? genericMapResponse.get(httpCode)
					: new ApiResponse();
			if (httpCode != null)
				buildApiResponses(components, methodParameter, apiResponsesOp, methodAttributes, httpCode, apiResponse,
						isGeneric);
		}
	}

	private Set<io.swagger.v3.oas.annotations.responses.ApiResponse> getApiResponses(Method method) {
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

	private Content buildContent(Components components, MethodParameter methodParameter, String[] methodProduces, JsonView jsonView) {
		Content content = new Content();
		Type returnType = getReturnType(methodParameter);
		if (isVoid(returnType)) {
			// if void, no content
			content = null;
		}
		else if (ArrayUtils.isNotEmpty(methodProduces)) {
			Schema<?> schemaN = calculateSchema(components, returnType, jsonView);
			if (schemaN != null) {
				io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
				mediaType.setSchema(schemaN);
				// Fill the content
				setContent(methodProduces, content, mediaType);
			}
		}
		return content;
	}

	private Type getReturnType(MethodParameter methodParameter) {
		Type returnType = Object.class;
		for (ReturnTypeParser returnTypeParser : returnTypeParsers) {
			if (returnType.getTypeName().equals(Object.class.getTypeName())) {
				returnType = returnTypeParser.getReturnType(methodParameter);
			}
			else {
				break;
			}
		}

		return returnType;
	}

	public Schema calculateSchema(Components components, Type returnType, JsonView jsonView) {
		if (isVoid(returnType)) {
			// if void, no content
			return null;
		}
		Schema schemaN = SpringDocAnnotationsUtils.extractSchema(components, returnType, jsonView);
		if (schemaN == null && returnType instanceof Class && !isResponseTypeToIgnore((Class) returnType)) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class) returnType, null, jsonView);
		}
		return schemaN;
	}

	private void setContent(String[] methodProduces, Content content,
			io.swagger.v3.oas.models.media.MediaType mediaType) {
		Arrays.stream(methodProduces).forEach(mediaTypeStr -> content.addMediaType(mediaTypeStr, mediaType));
	}

	private void buildApiResponses(Components components, MethodParameter methodParameter, ApiResponses apiResponsesOp,
			MethodAttributes methodAttributes, String httpCode, ApiResponse apiResponse, boolean isGeneric) {
		// No documentation
		if (StringUtils.isBlank(apiResponse.get$ref())) {
			if (apiResponse.getContent() == null) {
				Content content = buildContent(components, methodParameter, methodAttributes.getMethodProduces(),
						methodAttributes.getJsonViewAnnotation());
				apiResponse.setContent(content);
			}
			else if (CollectionUtils.isEmpty(apiResponse.getContent())) {
				apiResponse.setContent(null);
			}
			if (StringUtils.isBlank(apiResponse.getDescription())) {
				apiResponse.setDescription(DEFAULT_DESCRIPTION);
			}
		}
		if (apiResponse.getContent() != null
				&& ((isGeneric || methodAttributes.isMethodOverloaded()) && methodAttributes.isNoApiResponseDoc())) {
			// Merge with existing schema
			Content existingContent = apiResponse.getContent();
			Schema<?> schemaN = calculateSchema(components, methodParameter.getGenericParameterType(),
					methodAttributes.getJsonViewAnnotation());
			if (schemaN != null && ArrayUtils.isNotEmpty(methodAttributes.getMethodProduces())) {
				Arrays.stream(methodAttributes.getMethodProduces()).forEach(mediaTypeStr -> mergeSchema(existingContent, schemaN, mediaTypeStr));
			}
		}
		apiResponsesOp.addApiResponse(httpCode, apiResponse);
	}

	private void mergeSchema(Content existingContent, Schema<?> schemaN, String mediaTypeStr) {
		if (existingContent.containsKey(mediaTypeStr)) {
			io.swagger.v3.oas.models.media.MediaType mediaType = existingContent.get(mediaTypeStr);
			if (!schemaN.equals(mediaType.getSchema())) {
				// Merge the two schemas for the same mediaType
				Schema firstSchema = mediaType.getSchema();
				ComposedSchema schemaObject;
				if (firstSchema instanceof ComposedSchema) {
					schemaObject = (ComposedSchema) firstSchema;
					List<Schema> listOneOf = schemaObject.getOneOf();
					if (!CollectionUtils.isEmpty(listOneOf) && !listOneOf.contains(schemaN))
						schemaObject.addOneOfItem(schemaN);
				}
				else {
					schemaObject = new ComposedSchema();
					schemaObject.addOneOfItem(schemaN);
					schemaObject.addOneOfItem(firstSchema);
				}
				mediaType.setSchema(schemaObject);
				existingContent.addMediaType(mediaTypeStr, mediaType);
			}
		}
		else {
			// Add the new schema for a different mediaType
			existingContent.addMediaType(mediaTypeStr, new io.swagger.v3.oas.models.media.MediaType().schema(schemaN));
		}
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

	private boolean isVoid(Type returnType) {
		boolean result = false;
		if (Void.TYPE.equals(returnType))
			result = true;
		else if (returnType instanceof ParameterizedType) {
			Type[] types = ((ParameterizedType) returnType).getActualTypeArguments();
			if (types != null)
				return isVoid(types[0]);
		}
		if (Void.class.equals(returnType))
			result = true;
		return result;
	}
}
