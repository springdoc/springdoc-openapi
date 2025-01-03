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

package org.springdoc.core.providers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.data.ControllerType;
import org.springdoc.core.data.DataRestRepository;
import org.springdoc.core.data.DataRestRouterOperationService;
import org.springdoc.core.fn.RouterOperation;
import org.springdoc.core.utils.SpringDocDataRestUtils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.BasePathAwareHandlerMapping;
import org.springframework.data.rest.webmvc.ProfileController;
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
 *
 * @author bnasslahsen
 */
public class SpringRepositoryRestResourceProvider implements RepositoryRestResourceProvider, ApplicationContextAware {

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
	private static final String REPOSITORY_SEARCH_CONTROLLER = SPRING_DATA_REST_PACKAGE + ".webmvc.RepositorySearchController";

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
	 * The constant delegatingHandlerMappingClass.
	 */
	private static Class delegatingHandlerMappingClass;

	static {
		try {
			delegatingHandlerMappingClass = Class.forName(DELEGATING_HANDLER_MAPPING_CLASS);
		}
		catch (ClassNotFoundException e) {
			try {
				delegatingHandlerMappingClass = Class.forName(DELEGATING_HANDLER_MAPPING_INTERFACE);
			}
			catch (ClassNotFoundException exception) {
				LOGGER.trace(e.getMessage());
			}
		}
	}

	/**
	 * The Data rest router operation builder.
	 */
	private final DataRestRouterOperationService dataRestRouterOperationService;

	/**
	 * The Mapper.
	 */
	private final ObjectMapper mapper;

	/**
	 * The Application context.
	 */
	private ApplicationContext applicationContext;

	/**
	 * The Spring doc data rest utils.
	 */
	private final SpringDocDataRestUtils springDocDataRestUtils;

	/**
	 * The Handler mapping list.
	 */
	private List<HandlerMapping> handlerMappingList;

	/**
	 * Instantiates a new Spring repository rest resource provider.
	 *
	 * @param dataRestRouterOperationService the data rest router operation builder
	 * @param mapper                         the mapper
	 * @param springDocDataRestUtils         the spring doc data rest utils
	 */
	public SpringRepositoryRestResourceProvider(DataRestRouterOperationService dataRestRouterOperationService, 
			ObjectMapper mapper, SpringDocDataRestUtils springDocDataRestUtils) {
		this.dataRestRouterOperationService = dataRestRouterOperationService;
		this.mapper = mapper;
		this.springDocDataRestUtils = springDocDataRestUtils;
	}


	/**
	 * Gets router operations.
	 *
	 * @param openAPI the open api
	 * @param locale the locale
	 * @return the router operations
	 */
	public List<RouterOperation> getRouterOperations(OpenAPI openAPI, Locale locale) {
		List<RouterOperation> routerOperationList = new ArrayList<>();
		handlerMappingList = getHandlerMappingList();
		Associations associations = applicationContext.getBean(Associations.class);
		ResourceMappings mappings = applicationContext.getBean(ResourceMappings.class);
		PersistentEntities persistentEntities = applicationContext.getBean(PersistentEntities.class);
		Repositories repositories = applicationContext.getBean(Repositories.class);
		for (Class<?> domainType : repositories) {
			Class<?> repository = repositories.getRequiredRepositoryInformation(domainType).getRepositoryInterface();
			DataRestRepository dataRestRepository = new DataRestRepository(domainType, repository, locale);
			ResourceMetadata resourceMetadata = mappings.getMetadataFor(domainType);
			final PersistentEntity<?, ?> entity = persistentEntities.getRequiredPersistentEntity(domainType);
			dataRestRepository.setPersistentEntity(entity);
			final JacksonMetadata jackson = new JacksonMetadata(mapper, domainType);
			boolean hiddenRepository = (AnnotationUtils.findAnnotation(repository, Hidden.class) != null);
			if (!hiddenRepository) {
				if (resourceMetadata!=null && resourceMetadata.isExported()) {
					for (HandlerMapping handlerMapping : handlerMappingList) {
						if (handlerMapping instanceof RepositoryRestHandlerMapping repositoryRestHandlerMapping) {
							Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = repositoryRestHandlerMapping.getHandlerMethods();
							// Entity controllers lookup first
							Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFiltered = handlerMethodMap.entrySet().stream()
									.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_ENTITY_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
											.getValue().getBeanType().getName()))
									.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
							dataRestRepository.setControllerType(ControllerType.ENTITY);
							findControllers(routerOperationList, handlerMethodMapFiltered, resourceMetadata, dataRestRepository, openAPI);

							Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFilteredMethodMap = handlerMethodMap.entrySet().stream()
									.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_PROPERTY_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
											.getValue().getBeanType().getName()))
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
						else if (handlerMapping instanceof BasePathAwareHandlerMapping beanBasePathAwareHandlerMapping) {
							Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = beanBasePathAwareHandlerMapping.getHandlerMethods();
							Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFiltered = handlerMethodMap.entrySet().stream()
									.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_SCHEMA_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
											.getValue().getBeanType().getName()))
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
				findSearchResourceMappings(openAPI, routerOperationList, handlerMappingList, dataRestRepository, resourceMetadata, associations);
			}
		}
		return routerOperationList;
	}

	/**
	 * Gets Base Path Aware controller endpoints.
	 *
	 * @return the Base Path Aware controller endpoints
	 */
	@Override
	public Map<String, Object> getBasePathAwareControllerEndpoints() {
		return applicationContext.getBeansWithAnnotation(BasePathAwareController.class);
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
				.filter(entry -> !entry.getValue().getBeanType().getName().startsWith(SPRING_DATA_REST_PACKAGE) && AnnotatedElementUtils.hasAnnotation(entry.getValue().getBeanType(), BasePathAwareController.class))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * Customize.
	 *
	 * @param openAPI the open api
	 */
	@Override
	public void customize(OpenAPI openAPI) {
		ResourceMappings mappings = applicationContext.getBean(ResourceMappings.class);
		PersistentEntities persistentEntities = applicationContext.getBean(PersistentEntities.class);
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
			if (delegatingHandlerMappingClass != null) {
				Object object = applicationContext.getBean(delegatingHandlerMappingClass);
				try {
					handlerMappingList = (List<HandlerMapping>) MethodUtils.invokeMethod(object, "getDelegates");
				}
				catch (NoSuchMethodException | IllegalAccessException |
					   InvocationTargetException e) {
					LOGGER.warn(e.getMessage());
				}
			}
		}

		return handlerMappingList;
	}

	/**
	 * Find search resource mappings.
	 *
	 * @param openAPI             the open api
	 * @param routerOperationList the router operation list
	 * @param handlerMappingList  the handler mapping list
	 * @param dataRestRepository  the repository data rest
	 * @param resourceMetadata    the resource metadata
	 * @param associations        the associations
	 */
	private void findSearchResourceMappings(OpenAPI openAPI, List<RouterOperation> routerOperationList, List<HandlerMapping> handlerMappingList,
			DataRestRepository dataRestRepository, ResourceMetadata resourceMetadata, Associations associations) {
		for (HandlerMapping handlerMapping : handlerMappingList) {
			if (handlerMapping instanceof RepositoryRestHandlerMapping repositoryRestHandlerMapping) {
				Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = repositoryRestHandlerMapping.getHandlerMethods();
				Map<RequestMappingInfo, HandlerMethod> handlerMethodMapFiltered = handlerMethodMap.entrySet().stream()
						.filter(requestMappingInfoHandlerMethodEntry -> REPOSITORY_SEARCH_CONTROLLER.equals(requestMappingInfoHandlerMethodEntry
								.getValue().getBeanType().getName()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
				ResourceMetadata metadata = associations.getMetadataFor(dataRestRepository.getDomainType());
				if(metadata!=null && metadata.isExported()) {
					SearchResourceMappings searchResourceMappings = metadata.getSearchResourceMappings();
					if (searchResourceMappings.isExported()) {
						findSearchControllers(routerOperationList, handlerMethodMapFiltered, resourceMetadata, dataRestRepository, openAPI, searchResourceMappings);
					}
				}
			}
		}
	}

	/**
	 * Find search controllers list.
	 *
	 * @param routerOperationList    the router operation list
	 * @param handlerMethodMap       the handler method map
	 * @param resourceMetadata       the resource metadata
	 * @param dataRestRepository     the repository data rest
	 * @param openAPI                the open api
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
	 * @param handlerMethodMap    the handler method map
	 * @param resourceMetadata    the resource metadata
	 * @param dataRestRepository  the repository data rest
	 * @param openAPI             the open api
	 * @return the list
	 */
	private List<RouterOperation> findControllers(List<RouterOperation> routerOperationList,
			Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, ResourceMetadata resourceMetadata,
			DataRestRepository dataRestRepository, OpenAPI openAPI) {
		dataRestRouterOperationService.buildEntityRouterOperationList(routerOperationList, handlerMethodMap, resourceMetadata,
				dataRestRepository, openAPI);
		return routerOperationList;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}