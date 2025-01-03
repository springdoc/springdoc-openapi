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

package org.springdoc.core.configuration;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.configuration.hints.SpringDocDataRestHints;
import org.springdoc.core.converters.models.DefaultPageable;
import org.springdoc.core.data.DataRestOperationService;
import org.springdoc.core.data.DataRestRequestService;
import org.springdoc.core.data.DataRestResponseService;
import org.springdoc.core.data.DataRestRouterOperationService;
import org.springdoc.core.data.DataRestTagsService;
import org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.DataRestHalProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.SpringRepositoryRestResourceProvider;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericParameterService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.service.RequestBodyService;
import org.springdoc.core.utils.SpringDocDataRestUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.hateoas.server.LinkRelationProvider;

import static org.springdoc.core.utils.SpringDocUtils.getConfig;

/**
 * The type Spring doc data rest configuration.
 *
 * @author bnasslashen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and ${springdoc.enable-data-rest:true}")
@ConditionalOnClass(RepositoryRestConfiguration.class)
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration.class)
@ImportRuntimeHints(SpringDocDataRestHints.class)
public class SpringDocDataRestConfiguration {

	/**
	 * Hal provider data rest hal provider.
	 *
	 * @param repositoryRestConfiguration the repository rest configuration
	 * @param hateoasPropertiesOptional   the hateoas properties optional
	 * @param objectMapperProvider        the object mapper provider
	 * @return the data rest hal provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Primary
	@Lazy(false)
	DataRestHalProvider halProvider(Optional<RepositoryRestConfiguration> repositoryRestConfiguration, Optional<HateoasProperties> hateoasPropertiesOptional, ObjectMapperProvider objectMapperProvider) {
		return new DataRestHalProvider(repositoryRestConfiguration, hateoasPropertiesOptional, objectMapperProvider);
	}

	/**
	 * The type Spring repository rest resource provider configuration.
	 *
	 * @author bnasslashen
	 */
	@Lazy(false)
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(RepositoryRestHandlerMapping.class)
	static class SpringRepositoryRestResourceProviderConfiguration {

		static {
			getConfig().replaceParameterObjectWithClass(DefaultedPageable.class, DefaultPageable.class)
					.addRequestWrapperToIgnore(RootResourceInformation.class, PersistentEntityResourceAssembler.class, ETag.class, Sort.class)
					.addResponseWrapperToIgnore(RootResourceInformation.class);
		}

		/**
		 * Spring repository rest resource provider spring repository rest resource provider.
		 *
		 * @param dataRestRouterOperationService the data rest router operation service
		 * @param mapper                         the mapper
		 * @param springDocDataRestUtils         the spring doc data rest utils
		 * @return the spring repository rest resource provider
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		SpringRepositoryRestResourceProvider springRepositoryRestResourceProvider(DataRestRouterOperationService dataRestRouterOperationService, 
				ObjectMapper mapper, 
				SpringDocDataRestUtils springDocDataRestUtils) {
			return new SpringRepositoryRestResourceProvider(
					dataRestRouterOperationService, mapper, springDocDataRestUtils);
		}

		/**
		 * Data rest router operation builder data rest router operation service.
		 *
		 * @param dataRestOperationService    the data rest operation service
		 * @param springDocConfigProperties   the spring doc config properties
		 * @param repositoryRestConfiguration the repository rest configuration
		 * @param dataRestHalProvider         the data rest hal provider
		 * @return the data rest router operation service
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		DataRestRouterOperationService dataRestRouterOperationBuilder(DataRestOperationService dataRestOperationService,
				SpringDocConfigProperties springDocConfigProperties, RepositoryRestConfiguration repositoryRestConfiguration, DataRestHalProvider dataRestHalProvider) {
			return new DataRestRouterOperationService(dataRestOperationService, springDocConfigProperties, repositoryRestConfiguration, dataRestHalProvider);
		}

		/**
		 * Data rest operation builder data rest operation builder.
		 *
		 * @param dataRestRequestService  the data rest request builder
		 * @param tagsBuilder             the tags builder
		 * @param dataRestResponseService the data rest response builder
		 * @param operationService        the operation service
		 * @return the data rest operation builder
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		DataRestOperationService dataRestOperationBuilder(DataRestRequestService dataRestRequestService, DataRestTagsService tagsBuilder,
				DataRestResponseService dataRestResponseService, OperationService operationService) {
			return new DataRestOperationService(dataRestRequestService, tagsBuilder, dataRestResponseService, operationService);
		}

		/**
		 * Data rest request builder data rest request builder.
		 *
		 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
		 * @param parameterBuilder                      the parameter builder
		 * @param requestBodyService                    the request body builder
		 * @param requestBuilder                        the request builder
		 * @param springDocDataRestUtils                the spring doc data rest utils
		 * @return the data rest request builder
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		DataRestRequestService dataRestRequestBuilder(SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer, GenericParameterService parameterBuilder,
				RequestBodyService requestBodyService, AbstractRequestService requestBuilder, SpringDocDataRestUtils springDocDataRestUtils) {
			return new DataRestRequestService(localSpringDocParameterNameDiscoverer, parameterBuilder,
					requestBodyService, requestBuilder, springDocDataRestUtils);
		}

		/**
		 * Data rest response builder data rest response builder.
		 *
		 * @param genericResponseService the generic response builder
		 * @param springDocDataRestUtils the spring doc data rest utils
		 * @return the data rest response builder
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		DataRestResponseService dataRestResponseBuilder(GenericResponseService genericResponseService, SpringDocDataRestUtils springDocDataRestUtils) {
			return new DataRestResponseService(genericResponseService, springDocDataRestUtils);
		}

		/**
		 * Data rest tags builder data rest tags builder.
		 *
		 * @param openAPIService the open api builder
		 * @return the data rest tags builder
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		DataRestTagsService dataRestTagsBuilder(OpenAPIService openAPIService) {
			return new DataRestTagsService(openAPIService);
		}

		/**
		 * Spring doc data rest utils spring doc data rest utils.
		 *
		 * @param linkRelationProvider        the link relation provider
		 * @param repositoryRestConfiguration the repository rest configuration
		 * @return the spring doc data rest utils
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		SpringDocDataRestUtils springDocDataRestUtils(LinkRelationProvider linkRelationProvider, RepositoryRestConfiguration repositoryRestConfiguration) {
			return new SpringDocDataRestUtils(linkRelationProvider, repositoryRestConfiguration);
		}
	}

}
