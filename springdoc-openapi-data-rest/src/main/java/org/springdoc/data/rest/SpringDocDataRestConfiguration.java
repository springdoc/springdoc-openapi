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

import com.querydsl.core.types.Predicate;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.GenericParameterBuilder;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.RequestBodyBuilder;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.converters.models.DefaultPageable;
import org.springdoc.core.converters.models.Pageable;
import org.springdoc.data.rest.core.DataRestOperationBuilder;
import org.springdoc.data.rest.core.DataRestRequestBuilder;
import org.springdoc.data.rest.core.DataRestResponseBuilder;
import org.springdoc.data.rest.core.DataRestRouterOperationBuilder;
import org.springdoc.data.rest.core.DataRestTagsBuilder;
import org.springdoc.data.rest.customisers.QuerydslPredicateOperationCustomizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.rest.webmvc.support.DelegatingHandlerMapping;
import org.springframework.data.rest.webmvc.support.ETag;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

/**
 * The type Spring doc data rest configuration.
 * @author bnasslahsen
 */
@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocDataRestConfiguration {

	static {
		getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class, Pageable.class)
				.replaceWithClass(org.springframework.data.domain.PageRequest.class, Pageable.class)
				.replaceWithClass(DefaultedPageable.class, DefaultPageable.class)
				.addRequestWrapperToIgnore(RootResourceInformation.class, PersistentEntityResourceAssembler.class, ETag.class, Sort.class)
				.addResponseWrapperToIgnore(RootResourceInformation.class);
	}

	/**
	 * Hal provider data rest hal provider.
	 *
	 * @param repositoryRestConfiguration the repository rest configuration 
	 * @return the data rest hal provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Primary
	@Lazy(false)
	DataRestHalProvider halProvider(Optional<RepositoryRestConfiguration> repositoryRestConfiguration) {
		return new DataRestHalProvider(repositoryRestConfiguration);
	}


	/**
	 * The type Querydsl provider.
	 * @author bnasslahsen
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
	 * @author bnasslahsen
	 */
	@Configuration
	@ConditionalOnClass(RepositoryRestHandlerMapping.class)
	class SpringRepositoryRestResourceProviderConfiguration {

		/**
		 * Spring repository rest resource provider spring repository rest resource provider.
		 *
		 * @param mappings the mappings 
		 * @param repositories the repositories 
		 * @param associations the associations 
		 * @param delegatingHandlerMapping the delegating handler mapping 
		 * @param dataRestRouterOperationBuilder the data rest router operation builder 
		 * @return the spring repository rest resource provider
		 */
		@Bean
		@ConditionalOnMissingBean
		SpringRepositoryRestResourceProvider springRepositoryRestResourceProvider(ResourceMappings mappings,
				Repositories repositories, Associations associations, DelegatingHandlerMapping delegatingHandlerMapping,
				DataRestRouterOperationBuilder dataRestRouterOperationBuilder) {
			return new SpringRepositoryRestResourceProvider(mappings, repositories, associations,
					delegatingHandlerMapping, dataRestRouterOperationBuilder);
		}

		/**
		 * Data rest router operation builder data rest router operation builder.
		 *
		 * @param dataRestOperationBuilder the data rest operation builder 
		 * @param springDocConfigProperties the spring doc config properties 
		 * @param repositoryRestConfiguration the repository rest configuration 
		 * @param dataRestHalProvider the data rest hal provider 
		 * @return the data rest router operation builder
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestRouterOperationBuilder dataRestRouterOperationBuilder(DataRestOperationBuilder dataRestOperationBuilder,
				SpringDocConfigProperties springDocConfigProperties,RepositoryRestConfiguration repositoryRestConfiguration, DataRestHalProvider dataRestHalProvider) {
			return new DataRestRouterOperationBuilder(dataRestOperationBuilder, springDocConfigProperties, repositoryRestConfiguration, dataRestHalProvider);
		}

		/**
		 * Data rest operation builder data rest operation builder.
		 *
		 * @param dataRestRequestBuilder the data rest request builder 
		 * @param tagsBuilder the tags builder 
		 * @param dataRestResponseBuilder the data rest response builder 
		 * @return the data rest operation builder
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestOperationBuilder dataRestOperationBuilder(DataRestRequestBuilder dataRestRequestBuilder, DataRestTagsBuilder tagsBuilder,
				DataRestResponseBuilder dataRestResponseBuilder) {
			return new DataRestOperationBuilder(dataRestRequestBuilder, tagsBuilder, dataRestResponseBuilder);
		}

		/**
		 * Data rest request builder data rest request builder.
		 *
		 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer 
		 * @param parameterBuilder the parameter builder 
		 * @param requestBodyBuilder the request body builder 
		 * @param requestBuilder the request builder 
		 * @return the data rest request builder
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestRequestBuilder dataRestRequestBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, GenericParameterBuilder parameterBuilder,
				RequestBodyBuilder requestBodyBuilder, AbstractRequestBuilder requestBuilder) {
			return new DataRestRequestBuilder(localSpringDocParameterNameDiscoverer, parameterBuilder,
					requestBodyBuilder, requestBuilder);
		}

		/**
		 * Data rest response builder data rest response builder.
		 *
		 * @param genericResponseBuilder the generic response builder 
		 * @return the data rest response builder
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestResponseBuilder dataRestResponseBuilder(GenericResponseBuilder genericResponseBuilder) {
			return new DataRestResponseBuilder(genericResponseBuilder);
		}

		/**
		 * Data rest tags builder data rest tags builder.
		 *
		 * @param openAPIBuilder the open api builder 
		 * @return the data rest tags builder
		 */
		@Bean
		@ConditionalOnMissingBean
		DataRestTagsBuilder dataRestTagsBuilder(OpenAPIBuilder openAPIBuilder){
			return new DataRestTagsBuilder(openAPIBuilder);
		}
	}

}
