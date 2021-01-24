/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
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
 *
 */

package org.springdoc.data.rest.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;
import java.util.Set;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.MethodAttributes;
import org.springdoc.core.ReturnTypeParser;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

/**
 * The type Data rest response builder.
 * @author bnasslahsen
 */
public class DataRestResponseService {

	/**
	 * The Generic response builder.
	 */
	private GenericResponseService genericResponseService;

	/**
	 * Instantiates a new Data rest response builder.
	 *
	 * @param genericResponseService the generic response builder
	 */
	public DataRestResponseService(GenericResponseService genericResponseService) {
		this.genericResponseService = genericResponseService;
	}

	/**
	 * Build search response.
	 *
	 * @param operation the operation
	 * @param handlerMethod the handler method
	 * @param openAPI the open api
	 * @param methodResourceMapping the method resource mapping
	 * @param domainType the domain type
	 * @param methodAttributes the method attributes
	 */
	public void buildSearchResponse(Operation operation, HandlerMethod handlerMethod, OpenAPI openAPI,
			MethodResourceMapping methodResourceMapping, Class<?> domainType, MethodAttributes methodAttributes) {
		MethodParameter methodParameterReturn = handlerMethod.getReturnType();
		ApiResponses apiResponses;
		// check if @ApiResponse(s) is available on from @Operation annotation or method level
		Set<io.swagger.v3.oas.annotations.responses.ApiResponse> responsesArray = genericResponseService.getApiResponses(methodResourceMapping.getMethod());
		if (!responsesArray.isEmpty() || !CollectionUtils.isEmpty(operation.getResponses())) {
			apiResponses = genericResponseService.build(openAPI.getComponents(), new HandlerMethod(methodResourceMapping.getMethod().getDeclaringClass(), methodResourceMapping.getMethod()), operation, methodAttributes);
		}
		else {
			// Construct default response
			apiResponses = new ApiResponses();
			ApiResponse apiResponse = new ApiResponse();
			Type returnType = findSearchReturnType(handlerMethod, methodResourceMapping, domainType);
			Content content = genericResponseService.buildContent(openAPI.getComponents(), methodParameterReturn.getParameterAnnotations(), methodAttributes.getMethodProduces(), null, returnType);
			apiResponse.setContent(content);
			addResponse200(apiResponses, apiResponse);
			addResponse404(apiResponses);
		}
		operation.setResponses(apiResponses);
	}


	/**
	 * Build entity response.
	 *
	 * @param operation the operation
	 * @param handlerMethod the handler method
	 * @param openAPI the open api
	 * @param requestMethod the request method
	 * @param operationPath the operation path
	 * @param domainType the domain type
	 * @param methodAttributes the method attributes
	 */
	public void buildEntityResponse(Operation operation, HandlerMethod handlerMethod, OpenAPI openAPI, RequestMethod requestMethod,
			String operationPath, Class<?> domainType, MethodAttributes methodAttributes) {
		MethodParameter methodParameterReturn = handlerMethod.getReturnType();
		Type returnType = ReturnTypeParser.resolveType(methodParameterReturn.getGenericParameterType(), methodParameterReturn.getContainingClass());
		returnType = getType(returnType, domainType);
		ApiResponses apiResponses = new ApiResponses();
		ApiResponse apiResponse = new ApiResponse();
		Content content = genericResponseService.buildContent(openAPI.getComponents(), methodParameterReturn.getParameterAnnotations(), methodAttributes.getMethodProduces(), null, returnType);
		apiResponse.setContent(content);
		addResponse(requestMethod, operationPath, apiResponses, apiResponse);
		operation.setResponses(apiResponses);
	}

	/**
	 * Add response.
	 *
	 * @param requestMethod the request method
	 * @param operationPath the operation path
	 * @param apiResponses the api responses
	 * @param apiResponse the api response
	 */
	private void addResponse(RequestMethod requestMethod, String operationPath, ApiResponses apiResponses, ApiResponse apiResponse) {
		switch (requestMethod) {
			case GET:
				addResponse200(apiResponses, apiResponse);
				if (operationPath.contains("/{id}"))
					addResponse404(apiResponses);
				break;
			case POST:
				apiResponses.put(String.valueOf(HttpStatus.CREATED.value()), apiResponse.description(HttpStatus.CREATED.getReasonPhrase()));
				break;
			case DELETE:
				addResponse204(apiResponses);
				addResponse404(apiResponses);
				break;
			case PUT:
				addResponse200(apiResponses, apiResponse);
				apiResponses.put(String.valueOf(HttpStatus.CREATED.value()), new ApiResponse().content(apiResponse.getContent()).description(HttpStatus.CREATED.getReasonPhrase()));
				addResponse204(apiResponses);
				break;
			case PATCH:
				addResponse200(apiResponses, apiResponse);
				addResponse204(apiResponses);
				break;
			default:
				throw new IllegalArgumentException(requestMethod.name());
		}
	}

	/**
	 * Find search return type type.
	 *
	 * @param handlerMethod the handler method
	 * @param methodResourceMapping the method resource mapping
	 * @param domainType the domain type
	 * @return the type
	 */
	private Type findSearchReturnType(HandlerMethod handlerMethod, MethodResourceMapping methodResourceMapping, Class<?> domainType) {
		Type returnType;
		Type returnRepoType = ReturnTypeParser.resolveType(methodResourceMapping.getMethod().getGenericReturnType(), methodResourceMapping.getMethod().getDeclaringClass());
		if (methodResourceMapping.isPagingResource()) {
			returnType = ResolvableType.forClassWithGenerics(PagedModel.class, domainType).getType();
		}
		else if (ResolvableType.forType(returnRepoType).getRawClass() != null
				&& Iterable.class.isAssignableFrom(Objects.requireNonNull(ResolvableType.forType(returnRepoType).getRawClass()))) {
			returnType = ResolvableType.forClassWithGenerics(CollectionModel.class, domainType).getType();
		}
		else if (ClassUtils.isPrimitiveOrWrapper(methodResourceMapping.getReturnedDomainType())) {
			returnType = methodResourceMapping.getReturnedDomainType();
		}
		else {
			returnType = ResolvableType.forClassWithGenerics(EntityModel.class, domainType).getType();
		}
		return returnType;
	}

	/**
	 * Gets type.
	 *
	 * @param returnType the return type
	 * @param domainType the domain type
	 * @return the type
	 */
	private Type getType(Type returnType, Class<?> domainType) {
		if (returnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) returnType;
			if ((ResponseEntity.class.equals(parameterizedType.getRawType()))) {
				if (Object.class.equals(parameterizedType.getActualTypeArguments()[0])) {
					return ResolvableType.forClassWithGenerics(ResponseEntity.class, domainType).getType();
				}
				else if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType) {
					ParameterizedType parameterizedType1 = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
					Class<?> rawType = ResolvableType.forType(parameterizedType1.getRawType()).getRawClass();
					if (rawType != null && rawType.isAssignableFrom(RepresentationModel.class)) {
						return resolveGenericType(ResponseEntity.class, RepresentationModel.class, domainType);
					}
					else if (EntityModel.class.equals(parameterizedType1.getRawType())) {
						return resolveGenericType(ResponseEntity.class, EntityModel.class, domainType);
					}
				}
				else if (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType) {
					WildcardType wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
					if (wildcardType.getUpperBounds()[0] instanceof ParameterizedType) {
						ParameterizedType wildcardTypeUpperBound = (ParameterizedType) wildcardType.getUpperBounds()[0];
						if (RepresentationModel.class.equals(wildcardTypeUpperBound.getRawType())) {
							return resolveGenericType(ResponseEntity.class, RepresentationModel.class, domainType);
						}
					}
				}
			}
			else if ((HttpEntity.class.equals(parameterizedType.getRawType())
					&& parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType)) {
				ParameterizedType wildcardTypeUpperBound = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
				if (RepresentationModel.class.equals(wildcardTypeUpperBound.getRawType())) {
					return resolveGenericType(HttpEntity.class, RepresentationModel.class, domainType);
				}
			}
			else if ((CollectionModel.class.equals(parameterizedType.getRawType())
					&& Object.class.equals(parameterizedType.getActualTypeArguments()[0]))) {
				return ResolvableType.forClassWithGenerics(CollectionModel.class, domainType).getType();
			}
		}
		return returnType;
	}

	/**
	 * Resolve generic type type.
	 *
	 * @param container the container
	 * @param generic the generic
	 * @param domainType the domain type
	 * @return the type
	 */
	private Type resolveGenericType(Class<?> container, Class<?> generic, Class<?> domainType) {
		Type type = ResolvableType.forClassWithGenerics(generic, domainType).getType();
		return ResolvableType.forClassWithGenerics(container, ResolvableType.forType(type)).getType();
	}

	/**
	 * Add response 200.
	 *
	 * @param apiResponses the api responses
	 * @param apiResponse the api response
	 */
	private void addResponse200(ApiResponses apiResponses, ApiResponse apiResponse) {
		apiResponses.put(String.valueOf(HttpStatus.OK.value()), apiResponse.description(HttpStatus.OK.getReasonPhrase()));
	}

	/**
	 * Add response 204.
	 *
	 * @param apiResponses the api responses
	 */
	private void addResponse204(ApiResponses apiResponses) {
		apiResponses.put(String.valueOf(HttpStatus.NO_CONTENT.value()), new ApiResponse().description(HttpStatus.NO_CONTENT.getReasonPhrase()));
	}

	/**
	 * Add response 404.
	 *
	 * @param apiResponses the api responses
	 */
	private void addResponse404(ApiResponses apiResponses) {
		apiResponses.put(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse().description(HttpStatus.NOT_FOUND.getReasonPhrase()));
	}
}
