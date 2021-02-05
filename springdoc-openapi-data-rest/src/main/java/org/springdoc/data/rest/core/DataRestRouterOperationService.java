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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.MethodAttributes;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.fn.RouterOperation;
import org.springdoc.data.rest.DataRestHalProvider;
import org.springframework.data.rest.core.Path;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.HttpMethods;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.ResourceType;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

/**
 * The type Data rest router operation builder.
 * 
 * @author bnasslahsen
 */
public class DataRestRouterOperationService {

	/**
	 * The constant UNDOCUMENTED_REQUEST_METHODS.
	 */
	private static final List<RequestMethod> UNDOCUMENTED_REQUEST_METHODS = Arrays.asList(RequestMethod.OPTIONS,
			RequestMethod.HEAD);

	/**
	 * The constant REPOSITORY_PATH.
	 */
	private static final String REPOSITORY_PATH = AntPathMatcher.DEFAULT_PATH_SEPARATOR + "{repository}";

	/**
	 * The constant SEARCH_PATH.
	 */
	private static final String SEARCH_PATH = AntPathMatcher.DEFAULT_PATH_SEPARATOR + "{search}";

	/**
	 * The Data rest operation builder.
	 */
	private DataRestOperationService dataRestOperationService;

	/**
	 * The Spring doc config properties.
	 */
	private SpringDocConfigProperties springDocConfigProperties;

	/**
	 * Instantiates a new Data rest router operation builder.
	 * @param dataRestOperationService the data rest operation builder
	 * @param springDocConfigProperties the spring doc config properties
	 * @param repositoryRestConfiguration the repository rest configuration
	 * @param dataRestHalProvider the data rest hal provider
	 */
	public DataRestRouterOperationService(DataRestOperationService dataRestOperationService,
			SpringDocConfigProperties springDocConfigProperties,
			RepositoryRestConfiguration repositoryRestConfiguration, DataRestHalProvider dataRestHalProvider) {
		this.dataRestOperationService = dataRestOperationService;
		this.springDocConfigProperties = springDocConfigProperties;
		if (dataRestHalProvider.isHalEnabled())
			springDocConfigProperties
					.setDefaultProducesMediaType(repositoryRestConfiguration.getDefaultMediaType().toString());
	}

	/**
	 * Build entity router operation list.
	 * @param requestMappingInfo the request mapping info
	 * @param handlerMethod the handler method
	 * @param resourceMetadata the resource metadata
	 * @param openAPI the open api
	 * @return the router operation list
	 */
	public List<RouterOperation> buildCustomRouterOperationList(RequestMappingInfo requestMappingInfo,
			HandlerMethod handlerMethod, ResourceMetadata resourceMetadata, OpenAPI openAPI) {
		return requestMappingInfo.getMethodsCondition().getMethods().stream()
				.filter(requestMethod -> !UNDOCUMENTED_REQUEST_METHODS.contains(requestMethod))
				.flatMap(requestMethod -> {
					Set<String> patterns;
					PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
					if (patternsRequestCondition != null) {
						patterns = patternsRequestCondition.getPatterns();
					}
					else {
						PathPatternsRequestCondition pathPatternsCondition = requestMappingInfo
								.getPathPatternsCondition();
						patterns = pathPatternsCondition.getPatterns().stream().map(PathPattern::getPatternString)
								.collect(Collectors.toSet());
					}
					Map<String, String> regexMap = new LinkedHashMap<>();
					return patterns.stream()
							.map(pattern -> buildCustomRouterOperation(PathUtils.parsePath(pattern, regexMap),
									requestMethod, handlerMethod, resourceMetadata, openAPI));
				}).collect(Collectors.toList());
	}

	/**
	 * Build entity router operation list.
	 * @param handlerMethodMap the handler method map
	 * @param resourceMetadata the resource metadata
	 * @param dataRestRepository the repository data rest
	 * @param openAPI the open api
	 * @return the router operation list
	 */
	public List<RouterOperation> buildEntityRouterOperationList(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap,
			ResourceMetadata resourceMetadata, DataRestRepository dataRestRepository, OpenAPI openAPI) {
		List<RouterOperation> routerOperationList = new ArrayList<>();
		String path = resourceMetadata.getPath().toString();
		ControllerType controllerType = (dataRestRepository == null) ? ControllerType.SCHEMA
				: dataRestRepository.getControllerType();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
			routerOperationList.addAll(buildRouterOperationList(resourceMetadata, dataRestRepository, openAPI, path,
					entry, null, controllerType, null));
		}
		return routerOperationList;
	}

	/**
	 * Build search router operation list.
	 * @param handlerMethodMap the handler method map
	 * @param resourceMetadata the resource metadata
	 * @param dataRestRepository the repository data rest
	 * @param openAPI the open api
	 * @param methodResourceMapping the method resource mapping
	 * @return the router operation list
	 */
	public List<RouterOperation> buildSearchRouterOperationList(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap,
			ResourceMetadata resourceMetadata, DataRestRepository dataRestRepository, OpenAPI openAPI,
			MethodResourceMapping methodResourceMapping) {
		Optional<Entry<RequestMappingInfo, HandlerMethod>> entryOptional = getSearchEntry(handlerMethodMap);
		if (!entryOptional.isPresent()) {
			return new ArrayList<>();
		}
		String path = resourceMetadata.getPath().toString();
		Path subPath = methodResourceMapping.getPath();

		Entry<RequestMappingInfo, HandlerMethod> entry = entryOptional.get();
		return buildRouterOperationList(null, dataRestRepository, openAPI, path, entry, subPath.toString(),
				ControllerType.SEARCH, methodResourceMapping);
	}

	/**
	 * Build router operation list.
	 * @param resourceMetadata the resource metadata
	 * @param dataRestRepository the repository data rest
	 * @param openAPI the open api
	 * @param path the path
	 * @param entry the entry
	 * @param subPath the sub path
	 * @param controllerType the controllerType
	 * @param methodResourceMapping the method resource mapping
	 * @return the router operation list built
	 */
	private List<RouterOperation> buildRouterOperationList(ResourceMetadata resourceMetadata,
			DataRestRepository dataRestRepository, OpenAPI openAPI, String path,
			Entry<RequestMappingInfo, HandlerMethod> entry, String subPath, ControllerType controllerType,
			MethodResourceMapping methodResourceMapping) {
		RequestMappingInfo requestMappingInfo = entry.getKey();
		HandlerMethod handlerMethod = entry.getValue();
		Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
		if (resourceMetadata != null) {
			HttpMethods httpMethodsItem = resourceMetadata.getSupportedHttpMethods().getMethodsFor(ResourceType.ITEM);
			Set<RequestMethod> requestMethodsItem = requestMethods.stream()
					.filter(requestMethod -> httpMethodsItem.contains(HttpMethod.valueOf(requestMethod.toString())))
					.collect(Collectors.toSet());
			if (!ControllerType.PROPERTY.equals(controllerType)) {
				HttpMethods httpMethodsCollection = resourceMetadata.getSupportedHttpMethods()
						.getMethodsFor(ResourceType.COLLECTION);
				Set<RequestMethod> requestMethodsCollection = requestMethods.stream().filter(
						requestMethod -> httpMethodsCollection.contains(HttpMethod.valueOf(requestMethod.toString())))
						.collect(Collectors.toSet());
				requestMethodsItem.addAll(requestMethodsCollection);
			}
			requestMethods = requestMethodsItem;
		}

		List<RouterOperation> routerOperationList = new ArrayList<>();
		for (RequestMethod requestMethod : requestMethods) {
			if (!UNDOCUMENTED_REQUEST_METHODS.contains(requestMethod)) {
				PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
				if (patternsRequestCondition != null) {
					Set<String> patterns = patternsRequestCondition.getPatterns();
					Map<String, String> regexMap = new LinkedHashMap<>();
					String relationName = (dataRestRepository != null) ? dataRestRepository.getRelationName() : null;
					String operationPath = calculateOperationPath(path, subPath, patterns, regexMap, controllerType,
							relationName);
					routerOperationList.add(buildRouterOperation(dataRestRepository, openAPI, methodResourceMapping,
							handlerMethod, requestMethod, resourceMetadata, operationPath, controllerType));
				}
			}
		}
		return routerOperationList;
	}

	/**
	 * Calculate operation path string.
	 * @param path the path
	 * @param subPath the sub path
	 * @param patterns the patterns
	 * @param regexMap the regex map
	 * @param controllerType the controller type
	 * @param relationName the relation name
	 * @return the string
	 */
	private String calculateOperationPath(String path, String subPath, Set<String> patterns,
			Map<String, String> regexMap, ControllerType controllerType, String relationName) {
		String operationPath = null;
		for (String pattern : patterns) {
			operationPath = PathUtils.parsePath(pattern, regexMap);
			operationPath = operationPath.replace(REPOSITORY_PATH, path);
			if (ControllerType.SEARCH.equals(controllerType))
				operationPath = operationPath.replace(SEARCH_PATH, subPath);
			else if (ControllerType.PROPERTY.equals(controllerType))
				operationPath = operationPath.replace("{property}", relationName);
		}
		return operationPath;
	}

	/**
	 * Build router operation.
	 * @param dataRestRepository the repository data rest
	 * @param openAPI the open api
	 * @param methodResourceMapping the method resource mapping
	 * @param handlerMethod the handler method
	 * @param requestMethod the request method
	 * @param resourceMetadata the resource metadata
	 * @param operationPath the operation path
	 * @param controllerType the controller type
	 * @return the router operation built
	 */
	private RouterOperation buildRouterOperation(DataRestRepository dataRestRepository, OpenAPI openAPI,
			MethodResourceMapping methodResourceMapping, HandlerMethod handlerMethod, RequestMethod requestMethod,
			ResourceMetadata resourceMetadata, String operationPath, ControllerType controllerType) {
		RouterOperation routerOperation = new RouterOperation(operationPath, new RequestMethod[] { requestMethod });
		MethodAttributes methodAttributes = new MethodAttributes(
				springDocConfigProperties.getDefaultConsumesMediaType(),
				springDocConfigProperties.getDefaultProducesMediaType());
		methodAttributes.calculateConsumesProduces(handlerMethod.getMethod());
		routerOperation.setConsumes(methodAttributes.getMethodConsumes());
		routerOperation.setProduces(methodAttributes.getMethodProduces());
		Operation operation = dataRestOperationService.buildOperation(handlerMethod, dataRestRepository, openAPI,
				requestMethod, operationPath, methodAttributes, resourceMetadata, methodResourceMapping,
				controllerType);
		routerOperation.setOperationModel(operation);
		return routerOperation;
	}

	/**
	 * Build router operation.
	 * @param operationPath the operation path
	 * @param requestMethod the request method
	 * @param handlerMethod the handler method
	 * @param resourceMetadata the resource metadata
	 * @param openAPI the open api
	 * @return the router operation built
	 */
	private RouterOperation buildCustomRouterOperation(String operationPath, RequestMethod requestMethod,
			HandlerMethod handlerMethod, ResourceMetadata resourceMetadata, OpenAPI openAPI) {
		RouterOperation routerOperation = new RouterOperation(operationPath, new RequestMethod[] { requestMethod });
		MethodAttributes methodAttributes = new MethodAttributes(
				springDocConfigProperties.getDefaultConsumesMediaType(),
				springDocConfigProperties.getDefaultProducesMediaType());
		methodAttributes.calculateConsumesProduces(handlerMethod.getMethod());
		routerOperation.setConsumes(methodAttributes.getMethodConsumes());
		routerOperation.setProduces(methodAttributes.getMethodProduces());
		Operation operation = dataRestOperationService.buildCustomOperation(operationPath, requestMethod, handlerMethod,
				methodAttributes, resourceMetadata, openAPI);
		routerOperation.setOperationModel(operation);
		return routerOperation;
	}

	/**
	 * Gets search entry.
	 * @param handlerMethodMap the handler method map
	 * @return the search entry
	 */
	private Optional<Entry<RequestMappingInfo, HandlerMethod>> getSearchEntry(
			Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
		return handlerMethodMap.entrySet().stream().filter(requestMappingInfoHandlerMethodEntry -> {
			RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
			HandlerMethod handlerMethod = requestMappingInfoHandlerMethodEntry.getValue();
			Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
			for (RequestMethod requestMethod : requestMethods) {
				if (isSearchControllerPresent(requestMappingInfo, handlerMethod, requestMethod))
					return true;
			}
			return false;
		}).findAny();
	}

	/**
	 * Is search controller present boolean.
	 * @param requestMappingInfo the request mapping info
	 * @param handlerMethod the handler method
	 * @param requestMethod the request method
	 * @return the boolean
	 */
	private boolean isSearchControllerPresent(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod,
			RequestMethod requestMethod) {
		if (!UNDOCUMENTED_REQUEST_METHODS.contains(requestMethod)) {
			PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
			if (patternsRequestCondition != null) {
				Set<String> patterns = patternsRequestCondition.getPatterns();
				Map<String, String> regexMap = new LinkedHashMap<>();
				String operationPath;
				for (String pattern : patterns) {
					operationPath = PathUtils.parsePath(pattern, regexMap);
					if (operationPath.contains(REPOSITORY_PATH) && operationPath.contains(SEARCH_PATH)) {
						MethodAttributes methodAttributes = new MethodAttributes(
								springDocConfigProperties.getDefaultConsumesMediaType(),
								springDocConfigProperties.getDefaultProducesMediaType());
						methodAttributes.calculateConsumesProduces(handlerMethod.getMethod());
						if (springDocConfigProperties.getDefaultProducesMediaType()
								.equals(methodAttributes.getMethodProduces()[0]))
							return true;
					}
				}
			}
		}
		return false;
	}

}
