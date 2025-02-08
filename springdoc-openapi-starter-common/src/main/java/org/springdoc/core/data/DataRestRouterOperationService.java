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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.fn.RouterOperation;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.DataRestHalProvider;

import org.springframework.data.rest.core.Path;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.HttpMethods;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.ResourceType;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * The type Data rest router operation builder.
 *
 * @author bnasslahsen
 */
public class DataRestRouterOperationService {

	/**
	 * The constant UNDOCUMENTED_REQUEST_METHODS.
	 */
	private static final List<RequestMethod> UNDOCUMENTED_REQUEST_METHODS = Arrays.asList(RequestMethod.OPTIONS, RequestMethod.HEAD);

	/**
	 * The constant REPOSITORY_PATH.
	 */
	private static final String REPOSITORY_PATH = AntPathMatcher.DEFAULT_PATH_SEPARATOR + "{repository}";

	/**
	 * The constant SEARCH_PATH.
	 */
	private static final String SEARCH_PATH = AntPathMatcher.DEFAULT_PATH_SEPARATOR + "{search}";

	/**
	 * The constant ID.
	 */
	private static final String ID = "/{id}";

	/**
	 * The Data rest operation builder.
	 */
	private final DataRestOperationService dataRestOperationService;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * Instantiates a new Data rest router operation builder.
	 *
	 * @param dataRestOperationService    the data rest operation builder
	 * @param springDocConfigProperties   the spring doc config properties
	 * @param repositoryRestConfiguration the repository rest configuration
	 * @param dataRestHalProvider         the data rest hal provider
	 */
	public DataRestRouterOperationService(DataRestOperationService dataRestOperationService, SpringDocConfigProperties springDocConfigProperties,
			RepositoryRestConfiguration repositoryRestConfiguration, DataRestHalProvider dataRestHalProvider) {
		this.dataRestOperationService = dataRestOperationService;
		this.springDocConfigProperties = springDocConfigProperties;
		if (dataRestHalProvider.isHalEnabled())
			springDocConfigProperties.setDefaultProducesMediaType(repositoryRestConfiguration.getDefaultMediaType().toString());
	}

	/**
	 * Build entity router operation list.
	 *
	 * @param routerOperationList the router operation list
	 * @param handlerMethodMap    the handler method map
	 * @param resourceMetadata    the resource metadata
	 * @param dataRestRepository  the repository data rest
	 * @param openAPI             the open api
	 */
	public void buildEntityRouterOperationList(List<RouterOperation> routerOperationList,
			Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, ResourceMetadata resourceMetadata,
			DataRestRepository dataRestRepository, OpenAPI openAPI) {
		String path = resourceMetadata.getPath().toString();
		ControllerType controllerType = dataRestRepository.getControllerType();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
			buildRouterOperationList(routerOperationList, resourceMetadata, dataRestRepository, openAPI, path, entry, null, controllerType, null);
		}
	}

	/**
	 * Build search router operation list.
	 *
	 * @param routerOperationList   the router operation list
	 * @param handlerMethodMap      the handler method map
	 * @param resourceMetadata      the resource metadata
	 * @param dataRestRepository    the repository data rest
	 * @param openAPI               the open api
	 * @param methodResourceMapping the method resource mapping
	 */
	public void buildSearchRouterOperationList(List<RouterOperation> routerOperationList,
			Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, ResourceMetadata resourceMetadata,
			DataRestRepository dataRestRepository, OpenAPI openAPI, MethodResourceMapping methodResourceMapping) {
		String path = resourceMetadata.getPath().toString();
		Path subPath = methodResourceMapping.getPath();
		Optional<Entry<RequestMappingInfo, HandlerMethod>> entryOptional = getSearchEntry(handlerMethodMap, dataRestRepository.getLocale());
		if (entryOptional.isPresent()) {
			Entry<RequestMappingInfo, HandlerMethod> entry = entryOptional.get();
			buildRouterOperationList(routerOperationList, resourceMetadata, dataRestRepository, openAPI, path, entry, subPath.toString(), ControllerType.SEARCH, methodResourceMapping);
		}
	}

	/**
	 * Build router operation list.
	 *
	 * @param routerOperationList   the router operation list
	 * @param resourceMetadata      the resource metadata
	 * @param dataRestRepository    the repository data rest
	 * @param openAPI               the open api
	 * @param path                  the path
	 * @param entry                 the entry
	 * @param subPath               the sub path
	 * @param controllerType        the controllerType
	 * @param methodResourceMapping the method resource mapping
	 */
	private void buildRouterOperationList(List<RouterOperation> routerOperationList, ResourceMetadata resourceMetadata,
			DataRestRepository dataRestRepository, OpenAPI openAPI, String path, Entry<RequestMappingInfo, HandlerMethod> entry,
			String subPath, ControllerType controllerType, MethodResourceMapping methodResourceMapping) {
		RequestMappingInfo requestMappingInfo = entry.getKey();
		HandlerMethod handlerMethod = entry.getValue();
		Set<RequestMethod> requestMethodsItem;
		Set<RequestMethod> requestMethodsCollection;

		Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
		if (andCheck(resourceMetadata != null, !controllerType.equals(ControllerType.SEARCH))) {
			HttpMethods httpMethodsItem = resourceMetadata.getSupportedHttpMethods().getMethodsFor(ResourceType.ITEM);
			requestMethodsItem = requestMethods.stream().filter(requestMethod -> httpMethodsItem.contains(HttpMethod.valueOf(requestMethod.toString())))
					.collect(Collectors.toCollection(LinkedHashSet::new));

			buildRouterOperation(routerOperationList, resourceMetadata, dataRestRepository, openAPI, path,
					subPath, controllerType, methodResourceMapping, requestMappingInfo, handlerMethod, requestMethodsItem, ResourceType.ITEM);

			if (!ControllerType.PROPERTY.equals(controllerType)) {
				HttpMethods httpMethodsCollection = resourceMetadata.getSupportedHttpMethods().getMethodsFor(ResourceType.COLLECTION);
				requestMethodsCollection = requestMethods.stream().filter(requestMethod -> httpMethodsCollection.contains(HttpMethod.valueOf(requestMethod.toString())))
						.collect(Collectors.toCollection(LinkedHashSet::new));

				buildRouterOperation(routerOperationList, resourceMetadata, dataRestRepository, openAPI, path,
						subPath, controllerType, methodResourceMapping, requestMappingInfo, handlerMethod, requestMethodsCollection, ResourceType.COLLECTION);
			}

		}
		else {
			buildRouterOperation(routerOperationList, resourceMetadata, dataRestRepository, openAPI, path,
					subPath, controllerType, methodResourceMapping, requestMappingInfo, handlerMethod, requestMethods, null);
		}

	}

	/**
	 * Build router operation.
	 *
	 * @param routerOperationList      the router operation list
	 * @param resourceMetadata         the resource metadata
	 * @param dataRestRepository       the data rest repository
	 * @param openAPI                  the open api
	 * @param path                     the path
	 * @param subPath                  the sub path
	 * @param controllerType           the controller type
	 * @param methodResourceMapping    the method resource mapping
	 * @param requestMappingInfo       the request mapping info
	 * @param handlerMethod            the handler method
	 * @param requestMethodsCollection the request methods collection
	 * @param collection               the collection
	 */
	private void buildRouterOperation(List<RouterOperation> routerOperationList, ResourceMetadata resourceMetadata, DataRestRepository dataRestRepository,
			OpenAPI openAPI, String path, String subPath, ControllerType controllerType, MethodResourceMapping methodResourceMapping, RequestMappingInfo requestMappingInfo,
			HandlerMethod handlerMethod, Set<RequestMethod> requestMethodsCollection, ResourceType collection) {
		if (!CollectionUtils.isEmpty(requestMethodsCollection))
			for (RequestMethod requestMethod : requestMethodsCollection) {
				if (!UNDOCUMENTED_REQUEST_METHODS.contains(requestMethod)) {
					Set<String> patterns = getActivePatterns(requestMappingInfo);
					if (!CollectionUtils.isEmpty(patterns)) {
						Map<String, String> regexMap = new LinkedHashMap<>();
						String relationName = dataRestRepository.getRelationName();
						String operationPath = calculateOperationPath(path, subPath, patterns, regexMap, controllerType, relationName, collection);
						if (operationPath != null)
							buildRouterOperation(routerOperationList, dataRestRepository, openAPI, methodResourceMapping,
									handlerMethod, requestMethod, resourceMetadata, operationPath, controllerType);
					}
				}
			}
	}

	/**
	 * Calculate operation path string.
	 *
	 * @param path           the path
	 * @param subPath        the sub path
	 * @param patterns       the patterns
	 * @param regexMap       the regex map
	 * @param controllerType the controller type
	 * @param relationName   the relation name
	 * @param resourceType   the resource type
	 * @return the string
	 */
	private String calculateOperationPath(String path, String subPath, Set<String> patterns,
			Map<String, String> regexMap, ControllerType controllerType, String relationName, ResourceType resourceType) {
		String operationPath = null;
		for (String pattern : patterns) {
			operationPath = PathUtils.parsePath(pattern, regexMap);
			operationPath = operationPath.replace(REPOSITORY_PATH, path);
			if (ControllerType.ENTITY.equals(controllerType)) {
				if ((andCheck(ResourceType.ITEM.equals(resourceType), !operationPath.endsWith(ID))) || (andCheck(ResourceType.COLLECTION.equals(resourceType), operationPath.endsWith(ID))))
					operationPath = null;
			}
			else if (ControllerType.SEARCH.equals(controllerType))
				operationPath = operationPath.replace(SEARCH_PATH, subPath);
			else if (ControllerType.PROPERTY.equals(controllerType))
				operationPath = operationPath.replace("{property}", relationName);
		}
		return operationPath;
	}

	/**
	 * Build router operation.
	 *
	 * @param routerOperationList   the router operation list
	 * @param dataRestRepository    the repository data rest
	 * @param openAPI               the open api
	 * @param methodResourceMapping the method resource mapping
	 * @param handlerMethod         the handler method
	 * @param requestMethod         the request method
	 * @param resourceMetadata      the resource metadata
	 * @param operationPath         the operation path
	 * @param controllerType        the controller type
	 */
	private void buildRouterOperation
	(List<RouterOperation> routerOperationList, DataRestRepository
			dataRestRepository, OpenAPI openAPI,
			MethodResourceMapping methodResourceMapping, HandlerMethod handlerMethod,
			RequestMethod requestMethod, ResourceMetadata resourceMetadata, String
			operationPath, ControllerType controllerType) {
		RouterOperation routerOperation = new RouterOperation(operationPath, new RequestMethod[] { requestMethod }, null, null, null, null);
		MethodAttributes methodAttributes = new MethodAttributes(springDocConfigProperties.getDefaultConsumesMediaType(), springDocConfigProperties.getDefaultProducesMediaType(), dataRestRepository.getLocale());
		methodAttributes.calculateConsumesProduces(handlerMethod.getMethod());
		routerOperation.setConsumes(methodAttributes.getMethodConsumes());
		routerOperation.setProduces(methodAttributes.getMethodProduces());
		Operation operation = dataRestOperationService.buildOperation(handlerMethod, dataRestRepository,
				openAPI, requestMethod, operationPath, methodAttributes, resourceMetadata, methodResourceMapping, controllerType);
		routerOperation.setOperationModel(operation);
		routerOperationList.add(routerOperation);
	}


	/**
	 * Gets search entry.
	 *
	 * @param handlerMethodMap the handler method map
	 * @param locale           the locale
	 * @return the search entry
	 */
	private Optional<Entry<RequestMappingInfo, HandlerMethod>> getSearchEntry
	(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, Locale locale) {
		return handlerMethodMap.entrySet().stream().filter(
				requestMappingInfoHandlerMethodEntry -> {
					RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
					HandlerMethod handlerMethod = requestMappingInfoHandlerMethodEntry.getValue();
					Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
					for (RequestMethod requestMethod : requestMethods) {
						if (isSearchControllerPresent(requestMappingInfo, handlerMethod, requestMethod, locale))
							return true;
					}
					return false;
				}).findAny();
	}

	/**
	 * Is search controller present boolean.
	 *
	 * @param requestMappingInfo the request mapping info
	 * @param handlerMethod      the handler method
	 * @param requestMethod      the request method
	 * @param locale             the locale
	 * @return the boolean
	 */
	private boolean isSearchControllerPresent(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod, RequestMethod requestMethod, Locale locale) {
		if (!UNDOCUMENTED_REQUEST_METHODS.contains(requestMethod)) {
			Set<String> patterns = getActivePatterns(requestMappingInfo);
			if (!CollectionUtils.isEmpty(patterns)) {
				Map<String, String> regexMap = new LinkedHashMap<>();
				String operationPath;
				for (String pattern : patterns) {
					operationPath = PathUtils.parsePath(pattern, regexMap);
					if (andCheck(operationPath.contains(REPOSITORY_PATH), operationPath.contains(SEARCH_PATH))) {
						MethodAttributes methodAttributes = new MethodAttributes(springDocConfigProperties.getDefaultConsumesMediaType(), springDocConfigProperties.getDefaultProducesMediaType(), locale);
						methodAttributes.calculateConsumesProduces(handlerMethod.getMethod());
						if (springDocConfigProperties.getDefaultProducesMediaType().equals(methodAttributes.getMethodProduces()[0]))
							return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets active patterns.
	 *
	 * @param requestMappingInfo the request mapping info
	 * @return the active patterns
	 */
	private Set<String> getActivePatterns(RequestMappingInfo requestMappingInfo) {
		Set<String> patterns = null;
		PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
		if (patternsRequestCondition != null)
			patterns = patternsRequestCondition.getPatterns();
		else {
			PathPatternsRequestCondition pathPatternsRequestCondition = requestMappingInfo.getPathPatternsCondition();
			if (pathPatternsRequestCondition != null)
				patterns = pathPatternsRequestCondition.getPatternValues();
		}
		return patterns;
	}

	/**
	 * Is condition one and condition two boolean.
	 *
	 * @param conditionOne the condition one
	 * @param conditionTwo the condition two
	 * @return the boolean
	 */
	private boolean andCheck(boolean conditionOne, boolean conditionTwo) {
		return conditionOne && conditionTwo;
	}

}
