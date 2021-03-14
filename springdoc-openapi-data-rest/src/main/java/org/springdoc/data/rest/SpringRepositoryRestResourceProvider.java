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

package org.springdoc.data.rest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.RepositoryRestResourceProvider;
import org.springdoc.core.fn.RouterOperation;
import org.springdoc.data.rest.core.ControllerType;
import org.springdoc.data.rest.core.DataRestRepository;
import org.springdoc.data.rest.core.DataRestRouterOperationService;
import org.springdoc.data.rest.utils.SpringDocDataRestUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.data.rest.webmvc.BasePathAwareHandlerMapping;
import org.springframework.data.rest.webmvc.ProfileController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping;
import org.springframework.data.rest.webmvc.alps.AlpsController;
import org.springframework.data.rest.webmvc.json.JacksonMetadata;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

/**
 * The type Spring repository rest resource provider.
 * @author bnasslahsen
 */
public class SpringRepositoryRestResourceProvider implements RepositoryRestResourceProvider {

	/**
	 * The constant SPRING_DATA_REST_PACKAGE.
	 */
	private static final String SPRING_DATA_REST_PACKAGE = "org.springframework.data.rest";

	/**
	 * The constant REPOSITORY_SCHEMA_CONTROLLER.
	 */
	public static final String REPOSITORY_SCHEMA_CONTROLLER = SPRING_DATA_REST_PACKAGE + ".webmvc.RepositorySchemaController";

	/**
	 * The constant REPOSITORY_ENTITY_CONTROLLER.
	 */
	private static final String REPOSITORY_ENTITY_CONTROLLER = SPRING_DATA_REST_PACKAGE + ".webmvc.RepositoryEntityController";

	/**
	 * The constant REPOSITORY_SEARCH_CONTROLLER.
	 */
	private static final String REPOSITORY_SERACH_CONTROLLER = SPRING_DATA_REST_PACKAGE + ".webmvc.RepositorySearchController";

	/**
	 * The constant REPOSITORY_PROPERTY_CONTROLLER.
	 */
	private static final String REPOSITORY_PROPERTY_CONTROLLER = SPRING_DATA_REST_PACKAGE + ".webmvc.RepositoryPropertyReferenceController";

	/**
	 * The Delegating handler mapping class.
	 */
	private static final String DELEGATING_HANDLER_MAPPING_CLASS = SPRING_DATA_REST_PACKAGE + ".webmvc.config.DelegatingHandlerMapping";

	/**
	 * The Delegating handler mapping interface.
	 */
	private static final String DELEGATING_HANDLER_MAPPING_INTERFACE = SPRING_DATA_REST_PACKAGE + ".webmvc.support.DelegatingHandlerMapping";

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringRepositoryRestResourceProvider.class);

	/**
	 * The Mappings.
	 */
	private ResourceMappings mappings;

	/**
	 * The Repositories.
	 */
	private Repositories repositories;

	/**
	 * The Associations.
	 */
	private Associations associations;

	/**
	 * The Data rest router operation builder.
	 */
	private DataRestRouterOperationService dataRestRouterOperationService;

	/**
	 * The Persistent entities.
	 */
	private PersistentEntities persistentEntities;

	/**
	 * The Mapper.
	 */
	private ObjectMapper mapper;

	/**
	 * The Application context.
	 */
	private ApplicationContext applicationContext;

	/**
	 * The Handler mapping list.
	 */
	private List<HandlerMapping> handlerMappingList;

	/**
	 * The Spring doc data rest utils.
	 */
	private SpringDocDataRestUtils springDocDataRestUtils;

	/**
	 * Instantiates a new Spring repository rest resource provider.
	 *
	 * @param mappings the mappings
	 * @param repositories the repositories
	 * @param associations the associations
	 * @param applicationContext the application context
	 * @param dataRestRouterOperationService the data rest router operation builder
	 * @param persistentEntities the persistent entities
	 * @param mapper the mapper
	 */
	public SpringRepositoryRestResourceProvider(ResourceMappings mappings, Repositories repositories,
			Associations associations, ApplicationContext applicationContext, DataRestRouterOperationService dataRestRouterOperationService,
			PersistentEntities persistentEntities, ObjectMapper mapper, SpringDocDataRestUtils springDocDataRestUtils) {
		this.mappings = mappings;
		this.repositories = repositories;
		this.associations = associations;
		this.applicationContext = applicationContext;
		this.dataRestRouterOperationService = dataRestRouterOperationService;
		this.persistentEntities = persistentEntities;
		this.mapper = mapper;
		this.springDocDataRestUtils = springDocDataRestUtils;
	}


	public List<RouterOperation> getRouterOperations(OpenAPI openAPI) {
		List<RouterOperation> routerOperationList = new ArrayList<>();
		handlerMappingList = getHandlerMappingList();
		for (Class<?> domainType : repositories) {
			Class<?> repository = repositories.getRequiredRepositoryInformation(domainType).getRepositoryInterface();
			DataRestRepository dataRestRepository = new DataRestRepository(domainType, repository);
			ResourceMetadata resourceMetadata = mappings.getMetadataFor(domainType);
			final PersistentEntity<?, ?> entity = persistentEntities.getRequiredPersistentEntity(domainType);
			dataRestRepository.setPersistentEntity(entity);
			final JacksonMetadata jackson = new JacksonMetadata(mapper, domainType);

			if (resourceMetadata.isExported()) {
				for (HandlerMapping handlerMapping : handlerMappingList) {
					if (handlerMapping instanceof RepositoryRestHandlerMapping) {
						RepositoryRestHandlerMapping repositoryRestHandlerMapping = (RepositoryRestHandlerMapping) handlerMapping;
						Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = repositoryRestHandlerMapping.getHandlerMethods();
						// Entity controllers lookup first
						Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFiltered = handlerMethodMap.entrySet().stream()
								.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_ENTITY_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
										.getValue().getBeanType().getName()))
								.filter(controller -> !AbstractOpenApiResource.isHiddenRestControllers(controller.getValue().getBeanType()))
								.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
						dataRestRepository.setControllerType(ControllerType.ENTITY);
						findControllers(routerOperationList, handlerMethodMapFiltered, resourceMetadata, dataRestRepository, openAPI);

						Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFilteredMethodMap = handlerMethodMap.entrySet().stream()
								.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_PROPERTY_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
										.getValue().getBeanType().getName()))
								.filter(controller -> !AbstractOpenApiResource.isHiddenRestControllers(controller.getValue().getBeanType()))
								.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));

						entity.doWithAssociations((SimpleAssociationHandler) association -> {
							PersistentProperty<?> property = association.getInverse();
							if (jackson.isExported(property) && associations.isLinkableAssociation(property)) {
								dataRestRepository.setRelationName(resourceMetadata.getMappingFor(property).getRel().value());
								dataRestRepository.setControllerType(ControllerType.PROPERTY);
								dataRestRepository.setCollectionLike(property.isCollectionLike());
								dataRestRepository.setMap(property.isMap());
								dataRestRepository.setPropertyType(property.getActualType());
								findControllers(routerOperationList, handlerMethodMapFilteredMethodMap, resourceMetadata, dataRestRepository, openAPI);
							}
						});
					}
					else if (handlerMapping instanceof BasePathAwareHandlerMapping) {
						BasePathAwareHandlerMapping beanBasePathAwareHandlerMapping = (BasePathAwareHandlerMapping) handlerMapping;
						Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = beanBasePathAwareHandlerMapping.getHandlerMethods();
						Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFiltered = handlerMethodMap.entrySet().stream()
								.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_SCHEMA_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
										.getValue().getBeanType().getName()))
								.filter(controller -> !AbstractOpenApiResource.isHiddenRestControllers(controller.getValue().getBeanType()))
								.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
						dataRestRepository.setControllerType(ControllerType.SCHEMA);
						findControllers(routerOperationList, handlerMethodMapFiltered, resourceMetadata, dataRestRepository, openAPI);
						handlerMethodMapFiltered = handlerMethodMap.entrySet().stream()
								.filter(requestMappingInfoHandlerMethodEntry -> ProfileController.class.equals(requestMappingInfoHandlerMethodEntry
										.getValue().getBeanType()) || AlpsController.class.equals(requestMappingInfoHandlerMethodEntry
										.getValue().getBeanType()))
								.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
						dataRestRepository.setControllerType(ControllerType.GENERAL);
						findControllers(routerOperationList, handlerMethodMapFiltered, resourceMetadata, dataRestRepository, openAPI);
					}
				}
			}
			// search
			findSearchResourceMappings(openAPI, routerOperationList, handlerMappingList, dataRestRepository, resourceMetadata);
		}
		return routerOperationList;
	}

	/**
	 * Gets repository rest controller endpoints.
	 *
	 * @return the repository rest controller endpoints
	 */
	@Override
	public Map<String, Object> getRepositoryRestControllerEndpoints() {
		return applicationContext.getBeansWithAnnotation(RepositoryRestController.class);
	}

	/**
	 * Gets handler methods.
	 *
	 * @return the handler methods
	 */
	@Override
	public Map getHandlerMethods() {
		handlerMappingList = getHandlerMappingList();
		return handlerMappingList.stream().filter(RequestMappingInfoHandlerMapping.class::isInstance)
				.flatMap(
						handler -> ((RequestMappingInfoHandlerMapping) handler).getHandlerMethods().entrySet().stream())
				.filter(entry -> !entry.getValue().getBeanType().getName().startsWith(SPRING_DATA_REST_PACKAGE) && AnnotatedElementUtils.hasAnnotation(entry.getValue().getBeanType(), RepositoryRestController.class))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * Customize.
	 *
	 * @param openAPI the open api
	 */
	@Override
	public void customize(OpenAPI openAPI) {
		springDocDataRestUtils.customise(openAPI, mappings, persistentEntities);
	}

	/**
	 * Gets handler mapping list.
	 *
	 * @return the handler mapping list
	 */
	private List<HandlerMapping> getHandlerMappingList() {
		if (handlerMappingList == null) {
			handlerMappingList = new ArrayList<>();
			Class delegatingHandlerMappingClass = null;
			try {
				delegatingHandlerMappingClass = Class.forName(DELEGATING_HANDLER_MAPPING_CLASS);
			}
			catch (ClassNotFoundException e) {
				try {
					delegatingHandlerMappingClass = Class.forName(DELEGATING_HANDLER_MAPPING_INTERFACE);
				}
				catch (ClassNotFoundException exception) {
					LOGGER.warn(e.getMessage());
				}
			}
			if (delegatingHandlerMappingClass != null) {
				Object object = applicationContext.getBean(delegatingHandlerMappingClass);
				try {
					handlerMappingList = (List<HandlerMapping>) MethodUtils.invokeMethod(object, "getDelegates");
				}
				catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					LOGGER.warn(e.getMessage());
				}
			}
		}

		return handlerMappingList;
	}

	/**
	 * Find search resource mappings.
	 *
	 * @param openAPI the open api
	 * @param routerOperationList the router operation list
	 * @param handlerMappingList the handler mapping list
	 * @param dataRestRepository the repository data rest
	 * @param resourceMetadata the resource metadata
	 */
	private void findSearchResourceMappings(OpenAPI openAPI, List<RouterOperation> routerOperationList, List<HandlerMapping> handlerMappingList, DataRestRepository dataRestRepository, ResourceMetadata resourceMetadata) {
		for (HandlerMapping handlerMapping : handlerMappingList) {
			if (handlerMapping instanceof RepositoryRestHandlerMapping) {
				RepositoryRestHandlerMapping repositoryRestHandlerMapping = (RepositoryRestHandlerMapping) handlerMapping;
				Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = repositoryRestHandlerMapping.getHandlerMethods();
				Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFiltered = handlerMethodMap.entrySet().stream()
						.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_SERACH_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
								.getValue().getBeanType().getName()))
						.filter(controller -> !AbstractOpenApiResource.isHiddenRestControllers(controller.getValue().getBeanType()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
				ResourceMetadata metadata = associations.getMetadataFor(dataRestRepository.getDomainType());
				SearchResourceMappings searchResourceMappings = metadata.getSearchResourceMappings();
				if (searchResourceMappings.isExported()) {
					findSearchControllers(routerOperationList, handlerMethodMapFiltered, resourceMetadata, dataRestRepository, openAPI, searchResourceMappings);
				}
			}
		}
	}

	/**
	 * Find search controllers list.
	 *
	 * @param routerOperationList the router operation list
	 * @param handlerMethodMap the handler method map
	 * @param resourceMetadata the resource metadata
	 * @param dataRestRepository the repository data rest
	 * @param openAPI the open api
	 * @param searchResourceMappings the search resource mappings
	 * @return the list
	 */
	private List<RouterOperation> findSearchControllers(List<RouterOperation> routerOperationList,
			Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, ResourceMetadata resourceMetadata, DataRestRepository dataRestRepository,
			OpenAPI openAPI, SearchResourceMappings searchResourceMappings) {
		Stream<MethodResourceMapping> methodResourceMappingStream = searchResourceMappings.getExportedMappings();
		methodResourceMappingStream.forEach(methodResourceMapping -> dataRestRouterOperationService.buildSearchRouterOperationList(
				routerOperationList, handlerMethodMap, resourceMetadata, dataRestRepository, openAPI, methodResourceMapping));
		return routerOperationList;
	}


	/**
	 * Find controllers list.
	 *
	 * @param routerOperationList the router operation list
	 * @param handlerMethodMap the handler method map
	 * @param resourceMetadata the resource metadata
	 * @param dataRestRepository the repository data rest
	 * @param openAPI the open api
	 * @return the list
	 */
	private List<RouterOperation> findControllers(List<RouterOperation> routerOperationList,
			Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, ResourceMetadata resourceMetadata,
			DataRestRepository dataRestRepository, OpenAPI openAPI) {
		dataRestRouterOperationService.buildEntityRouterOperationList(routerOperationList, handlerMethodMap, resourceMetadata,
				dataRestRepository, openAPI);
		return routerOperationList;
	}

}