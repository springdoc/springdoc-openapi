/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package org.springdoc.core.data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.utils.SpringDocDataRestUtils;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
 *
 * @author bnasslahsen
 */
public class DataRestResponseService {

	/**
	 * The constant requestMethodsEntityModel.
	 */
	private static final RequestMethod[] requestMethodsEntityModel = { RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT };

	/**
	 * The Generic response builder.
	 */
	private final GenericResponseService genericResponseService;

	/**
	 * The Spring doc data rest utils.
	 */
	private final SpringDocDataRestUtils springDocDataRestUtils;

	/**
	 * Instantiates a new Data rest response builder.
	 *
	 * @param genericResponseService the generic response builder
	 * @param springDocDataRestUtils the spring doc data rest utils
	 */
	public DataRestResponseService(GenericResponseService genericResponseService, SpringDocDataRestUtils springDocDataRestUtils) {
		this.genericResponseService = genericResponseService;
		this.springDocDataRestUtils = springDocDataRestUtils;
	}

	/**
	 * Build search response.
	 *
	 * @param operation             the operation
	 * @param handlerMethod         the handler method
	 * @param openAPI               the open api
	 * @param methodResourceMapping the method resource mapping
	 * @param domainType            the domain type
	 * @param methodAttributes      the method attributes
	 * @param resourceMetadata      the resource metadata
	 * @param dataRestRepository    the data rest repository
	 */
	public void buildSearchResponse(Operation operation, HandlerMethod handlerMethod, OpenAPI openAPI,
			MethodResourceMapping methodResourceMapping, Class<?> domainType, MethodAttributes methodAttributes, ResourceMetadata resourceMetadata,
			DataRestRepository dataRestRepository) {
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
			Type returnType = findSearchReturnType(methodResourceMapping, domainType);
			Content content = genericResponseService.buildContent(openAPI.getComponents(), methodParameterReturn.getParameterAnnotations(), methodAttributes.getMethodProduces(), null, returnType);
			springDocDataRestUtils.buildTextUriContent(content);
			apiResponse.setContent(content);
			addResponse200(apiResponses, apiResponse);
			addResponse404(apiResponses);
		}
		operation.setResponses(apiResponses);
	}


	/**
	 * Build entity response.
	 *
	 * @param operation          the operation
	 * @param handlerMethod      the handler method
	 * @param openAPI            the open api
	 * @param requestMethod      the request method
	 * @param operationPath      the operation path
	 * @param methodAttributes   the method attributes
	 * @param dataRestRepository the data rest repository
	 * @param resourceMetadata   the resource metadata
	 */
	public void buildEntityResponse(Operation operation, HandlerMethod handlerMethod, OpenAPI openAPI, RequestMethod requestMethod,
			String operationPath, MethodAttributes methodAttributes, DataRestRepository dataRestRepository, ResourceMetadata resourceMetadata) {
		MethodParameter methodParameterReturn = handlerMethod.getReturnType();
		Type returnType = getType(methodParameterReturn, requestMethod, dataRestRepository, resourceMetadata);
		ApiResponses apiResponses = new ApiResponses();
		ApiResponse apiResponse = new ApiResponse();
		Content content = genericResponseService.buildContent(openAPI.getComponents(), methodParameterReturn.getParameterAnnotations(), methodAttributes.getMethodProduces(), null, returnType);
		springDocDataRestUtils.buildTextUriContent(content);
		apiResponse.setContent(content);
		addResponse(requestMethod, operationPath, apiResponses, apiResponse);
		operation.setResponses(apiResponses);
	}

	/**
	 * Add response.
	 *
	 * @param requestMethod the request method
	 * @param operationPath the operation path
	 * @param apiResponses  the api responses
	 * @param apiResponse   the api response
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
	 * Find search return type.
	 *
	 * @param methodResourceMapping the method resource mapping
	 * @param domainType            the domain type
	 * @return the type
	 */
	private Type findSearchReturnType(MethodResourceMapping methodResourceMapping, Class<?> domainType) {
		Type returnType;
		Type returnRepoType = GenericTypeResolver.resolveType(methodResourceMapping.getMethod().getGenericReturnType(), methodResourceMapping.getMethod().getDeclaringClass());
		if (methodResourceMapping.isPagingResource()) {
			returnType = resolveGenericType(PagedModel.class, EntityModel.class, domainType);
		}
		else if (ResolvableType.forType(returnRepoType).getRawClass() != null
				&& Iterable.class.isAssignableFrom(Objects.requireNonNull(ResolvableType.forType(returnRepoType).getRawClass()))) {
			return getTypeForCollectionModel(domainType, methodResourceMapping.isPagingResource());
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
	 * @param methodParameterReturn the method parameter return
	 * @param requestMethod         the request method
	 * @param dataRestRepository    the data rest repository
	 * @param resourceMetadata      the resource metadata
	 * @return the type
	 */
	private Type getType(MethodParameter methodParameterReturn, RequestMethod requestMethod, DataRestRepository dataRestRepository, ResourceMetadata resourceMetadata) {
		Type returnType = GenericTypeResolver.resolveType(methodParameterReturn.getGenericParameterType(), methodParameterReturn.getContainingClass());
		Class returnedEntityType = dataRestRepository.getReturnType();

		if (returnType instanceof ParameterizedType parameterizedType) {
			if ((ResponseEntity.class.equals(parameterizedType.getRawType()))) {
				return getTypeForResponseEntity(requestMethod, dataRestRepository, returnedEntityType, parameterizedType);
			}
			else if ((HttpEntity.class.equals(parameterizedType.getRawType())
					&& parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType wildcardTypeUpperBound)) {
				if (RepresentationModel.class.equals(wildcardTypeUpperBound.getRawType())) {
					return resolveGenericType(HttpEntity.class, RepresentationModel.class, returnedEntityType);
				}
			}
			else if ((CollectionModel.class.equals(parameterizedType.getRawType())
					&& parameterizedType.getActualTypeArguments()[0]!=null)) {
				return getTypeForCollectionModel(returnedEntityType, resourceMetadata.isPagingResource());
			}
		}
		return returnType;
	}

	/**
	 * Gets type for response entity.
	 *
	 * @param requestMethod      the request method
	 * @param dataRestRepository the data rest repository
	 * @param returnedEntityType the returned entity type
	 * @param parameterizedType  the parameterized type
	 * @return the type for response entity
	 */
	private Type getTypeForResponseEntity(RequestMethod requestMethod, DataRestRepository dataRestRepository, Class returnedEntityType, ParameterizedType parameterizedType) {
		if (Object.class.equals(parameterizedType.getActualTypeArguments()[0])) {
			return ResolvableType.forClassWithGenerics(ResponseEntity.class, returnedEntityType).getType();
		}
		else if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType) {
			return getTypeForParameterizedType(requestMethod, dataRestRepository, returnedEntityType, parameterizedType);
		}
		else if (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType) {
			return getTypeForWildcardType(requestMethod, dataRestRepository, returnedEntityType, parameterizedType);
		}
		return null;
	}

	/**
	 * Gets type for collection model.
	 *
	 * @param returnedEntityType the returned entity type
	 * @param pagingResource     the paging resource
	 * @return the type for collection model
	 */
	private Type getTypeForCollectionModel(Class returnedEntityType, boolean pagingResource) {
		if (pagingResource)
			return resolveGenericType(PagedModel.class, EntityModel.class, returnedEntityType);
		else
			return resolveGenericType(CollectionModel.class, EntityModel.class, returnedEntityType);
	}

	/**
	 * Gets type for wildcard type.
	 *
	 * @param requestMethod      the request method
	 * @param dataRestRepository the data rest repository
	 * @param returnedEntityType the returned entity type
	 * @param parameterizedType  the parameterized type
	 * @return the type for wildcard type
	 */
	private Type getTypeForWildcardType(RequestMethod requestMethod, DataRestRepository dataRestRepository, Class returnedEntityType, ParameterizedType parameterizedType) {
		WildcardType wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
		if (wildcardType.getUpperBounds()[0] instanceof ParameterizedType wildcardTypeUpperBound) {
			if (RepresentationModel.class.equals(wildcardTypeUpperBound.getRawType())) {
				Class<?> type = findType(requestMethod, dataRestRepository);
				if (MapModel.class.equals(type))
					return ResolvableType.forClassWithGenerics(ResponseEntity.class, type).getType();
				else
					return resolveGenericType(ResponseEntity.class, type, returnedEntityType);
			}
		}
		return null;
	}

	/**
	 * Gets type.
	 *
	 * @param requestMethod      the request method
	 * @param dataRestRepository the data rest repository
	 * @param returnedEntityType the returned entity type
	 * @param parameterizedType  the parameterized type
	 * @return the type
	 */
	private Type getTypeForParameterizedType(RequestMethod requestMethod, DataRestRepository dataRestRepository, Class returnedEntityType, ParameterizedType parameterizedType) {
		ParameterizedType parameterizedType1 = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
		Class<?> rawType = ResolvableType.forType(parameterizedType1.getRawType()).getRawClass();
		if (rawType != null && rawType.isAssignableFrom(RepresentationModel.class)) {
			Class<?> type = findType(requestMethod, dataRestRepository);
			if (MapModel.class.equals(type))
				return ResolvableType.forClassWithGenerics(ResponseEntity.class, type).getType();
			else
				return resolveGenericType(ResponseEntity.class, type, returnedEntityType);
		}
		else if (EntityModel.class.equals(parameterizedType1.getRawType())) {
			return resolveGenericType(ResponseEntity.class, EntityModel.class, returnedEntityType);
		}
		return null;
	}

	/**
	 * Find type class.
	 *
	 * @param requestMethod      the request method
	 * @param dataRestRepository the data rest repository
	 * @return the class
	 */
	private Class findType(RequestMethod requestMethod, DataRestRepository dataRestRepository) {
		if (ControllerType.ENTITY.equals(dataRestRepository.getControllerType())
				&& Arrays.stream(requestMethodsEntityModel).anyMatch(requestMethod::equals))
			return EntityModel.class;
		else if (ControllerType.PROPERTY.equals(dataRestRepository.getControllerType())) {
			if (dataRestRepository.isCollectionLike())
				return CollectionModel.class;
			else if (dataRestRepository.isMap())
				return MapModel.class;
			else
				return EntityModel.class;
		}
		else
			return RepresentationModel.class;
	}

	/**
	 * Resolve generic type type.
	 *
	 * @param container  the container
	 * @param generic    the generic
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
	 * @param apiResponse  the api response
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

	/**
	 * The type Map model.
	 *
	 * @author bnasslashen
	 */
	private static class MapModel extends RepresentationModel<MapModel> {
		/**
		 * The Content.
		 */
		private final Map content;

		/**
		 * Instantiates a new Map model.
		 *
		 * @param content the content
		 * @param links   the links
		 */
		public MapModel(Map content, Link... links) {
			super(Arrays.asList(links));
			this.content = content;
		}

		/**
		 * Gets content.
		 *
		 * @return the content
		 */
		@JsonAnyGetter
		public Map getContent() {
			return content;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			if (!super.equals(o)) return false;
			MapModel mapModel = (MapModel) o;
			return Objects.equals(getContent(), mapModel.getContent());
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), getContent());
		}
	}

}
