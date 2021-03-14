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

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.GenericParameterService;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.RequestBodyService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.converters.models.DefaultPageable;
import org.springdoc.core.converters.models.Pageable;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;
import org.springdoc.data.rest.core.DataRestOperationService;
import org.springdoc.data.rest.core.DataRestRequestService;
import org.springdoc.data.rest.core.DataRestResponseService;
import org.springdoc.data.rest.core.DataRestRouterOperationService;
import org.springdoc.data.rest.core.DataRestTagsService;
import org.springdoc.data.rest.customisers.DataRestDelegatingMethodParameterCustomizer;
import org.springdoc.data.rest.customisers.QuerydslPredicateOperationCustomizer;
import org.springdoc.data.rest.utils.SpringDocDataRestUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.hateoas.server.LinkRelationProvider;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

/**
 * The type Spring doc data rest configuration.
 * @author bnasslashen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocDataRestConfiguration {

	static {
		getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class, Pageable.class)
				.replaceWithClass(org.springframework.data.domain.PageRequest.class, Pageable.class);
	}

	/**
	 * Delegating method parameter customizer delegating method parameter customizer.
	 *
	 * @param optionalSpringDataWebProperties the optional spring data web properties
	 * @param optionalRepositoryRestConfiguration the optional repository rest configuration
	 * @return the delegating method parameter customizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	DelegatingMethodParameterCustomizer delegatingMethodParameterCustomizer(Optional<SpringDataWebProperties> optionalSpringDataWebProperties, Optional<RepositoryRestConfiguration> optionalRepositoryRestConfiguration) {
		return new DataRestDelegatingMethodParameterCustomizer(optionalSpringDataWebProperties, optionalRepositoryRestConfiguration);
	}

	/**
	 * Hal provider data rest hal provider.
	 *
	 * @param repositoryRestConfiguration the repository rest configuration
	 * @param hateoasPropertiesOptional the hateoas properties optional
	 * @return the data rest hal provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Primary
	@Lazy(false)
	DataRestHalProvider halProvider(Optional<RepositoryRestConfiguration> repositoryRestConfiguration, Optional<HateoasProperties> hateoasPropertiesOptional) {
		return new DataRestHalProvider(repositoryRestConfiguration, hateoasPropertiesOptional);
	}


	/**
	 * The type Querydsl provider.
	 * @author bnasslashen
	 */
	@ConditionalOnClass(value = { QuerydslBindingsFactory.class })
	class QuerydslProvider {

		/**
		 * Query dsl querydsl predicate operation customizer querydsl predicate operation customizer.
		 *
		 * @param querydslBindingsFactory the querydsl bindings factory
		 * @return the querydsl predicate operation customizer
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		QuerydslPredicateOperationCustomizer queryDslQuerydslPredicateOperationCustomizer(Optional<QuerydslBindingsFactory> querydslBindingsFactory) {
			if (querydslBindingsFactory.isPresent()) {
				getConfig().addRequestWrapperToIgnore(Predicate.class);
				return new QuerydslPredicateOperationCustomizer(querydslBindingsFactory.get());
			}
			return null;
		}
	}

	/**
	 * The type Spring repository rest resource provider configuration.
	 * @author bnasslashen
	 */
	@Lazy(false)
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(RepositoryRestHandlerMapping.class)
	static class SpringRepositoryRestResourceProviderConfiguration {

		static {
			getConfig().replaceWithClass(DefaultedPageable.class, DefaultPageable.class)
					.addRequestWrapperToIgnore(RootResourceInformation.class, PersistentEntityResourceAssembler.class, ETag.class, Sort.class)
					.addResponseWrapperToIgnore(RootResourceInformation.class);
		}

		/**
		 * Spring repository rest resource provider spring repository rest resource provider.
		 *
		 * @param mappings the mappings
		 * @param repositories the repositories
		 * @param associations the associations
		 * @param applicationContext the application context
		 * @param dataRestRouterOperationService the data rest router operation service
		 * @param persistentEntities the persistent entities
		 * @param mapper the mapper
		 * @return the spring repository rest resource provider
		 */
		@Bean
		@ConditionalOnMissingBean
		SpringRepositoryRestResourceProvider springRepositoryRestResourceProvider(ResourceMappings mappings,
				Repositories repositories, Associations associations, ApplicationContext applicationContext,
				DataRestRouterOperationService dataRestRouterOperationService, PersistentEntities persistentEntities,
				ObjectMapper mapper,SpringDocDataRestUtils springDocDataRestUtils) {
			return new SpringRepositoryRestResourceProvider(mappings, repositories, associations, applicationContext,
					dataRestRouterOperationService, persistentEntities, mapper, springDocDataRestUtils);
		}

		/**
		 * Data rest router operation builder data rest router operation service.
		 *
		 * @param dataRestOperationService the data rest operation service
		 * @param springDocConfigProperties the spring doc config properties
		 * @param repositoryRestConfiguration the repository rest configuration
		 * @param dataRestHalProvider the data rest hal provider
		 * @return the data rest router operation service
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestRouterOperationService dataRestRouterOperationBuilder(DataRestOperationService dataRestOperationService,
				SpringDocConfigProperties springDocConfigProperties, RepositoryRestConfiguration repositoryRestConfiguration, DataRestHalProvider dataRestHalProvider) {
			return new DataRestRouterOperationService(dataRestOperationService, springDocConfigProperties, repositoryRestConfiguration, dataRestHalProvider);
		}

		/**
		 * Data rest operation builder data rest operation builder.
		 *
		 * @param dataRestRequestService the data rest request builder
		 * @param tagsBuilder the tags builder
		 * @param dataRestResponseService the data rest response builder
		 * @param operationService the operation service
		 * @return the data rest operation builder
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestOperationService dataRestOperationBuilder(DataRestRequestService dataRestRequestService, DataRestTagsService tagsBuilder,
				DataRestResponseService dataRestResponseService, OperationService operationService) {
			return new DataRestOperationService(dataRestRequestService, tagsBuilder, dataRestResponseService, operationService);
		}

		/**
		 * Data rest request builder data rest request builder.
		 *
		 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
		 * @param parameterBuilder the parameter builder
		 * @param requestBodyService the request body builder
		 * @param requestBuilder the request builder
		 * @param springDocDataRestUtils the spring doc data rest utils
		 * @return the data rest request builder
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestRequestService dataRestRequestBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, GenericParameterService parameterBuilder,
				RequestBodyService requestBodyService, AbstractRequestService requestBuilder,SpringDocDataRestUtils springDocDataRestUtils) {
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
		DataRestTagsService dataRestTagsBuilder(OpenAPIService openAPIService) {
			return new DataRestTagsService(openAPIService);
		}

		/**
		 * Spring doc data rest utils spring doc data rest utils.
		 *
		 * @param linkRelationProvider the link relation provider
		 * @return the spring doc data rest utils
		 */
		@Bean
		@ConditionalOnMissingBean
		SpringDocDataRestUtils springDocDataRestUtils(LinkRelationProvider linkRelationProvider) {
			return new SpringDocDataRestUtils(linkRelationProvider);
		}
	}

}
