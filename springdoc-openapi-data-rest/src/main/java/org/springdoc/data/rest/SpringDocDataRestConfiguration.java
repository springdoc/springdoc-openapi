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

	@Bean
	@ConditionalOnMissingBean
	@Primary
	@Lazy(false)
	DataRestHalProvider halProvider(Optional<RepositoryRestConfiguration> repositoryRestConfiguration) {
		return new DataRestHalProvider(repositoryRestConfiguration);
	}


	@ConditionalOnClass(value = { QuerydslBindingsFactory.class })
	class QuerydslProvider {

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

	@Configuration
	@ConditionalOnClass(RepositoryRestHandlerMapping.class)
	class SpringRepositoryRestResourceProviderConfiguration {

		@Bean
		@ConditionalOnMissingBean
		SpringRepositoryRestResourceProvider springRepositoryRestResourceProvider(ResourceMappings mappings,
				Repositories repositories, Associations associations, DelegatingHandlerMapping delegatingHandlerMapping,
				DataRestRouterOperationBuilder dataRestRouterOperationBuilder) {
			return new SpringRepositoryRestResourceProvider(mappings, repositories, associations,
					delegatingHandlerMapping, dataRestRouterOperationBuilder);
		}

		@Bean
		@ConditionalOnMissingBean
		DataRestRouterOperationBuilder dataRestRouterOperationBuilder(DataRestOperationBuilder dataRestOperationBuilder,
				SpringDocConfigProperties springDocConfigProperties,RepositoryRestConfiguration repositoryRestConfiguration, DataRestHalProvider dataRestHalProvider) {
			return new DataRestRouterOperationBuilder(dataRestOperationBuilder, springDocConfigProperties, repositoryRestConfiguration, dataRestHalProvider);
		}

		@Bean
		@ConditionalOnMissingBean
		DataRestOperationBuilder dataRestOperationBuilder(DataRestRequestBuilder dataRestRequestBuilder, DataRestTagsBuilder tagsBuilder,
				DataRestResponseBuilder dataRestResponseBuilder) {
			return new DataRestOperationBuilder(dataRestRequestBuilder, tagsBuilder, dataRestResponseBuilder);
		}

		@Bean
		@ConditionalOnMissingBean
		DataRestRequestBuilder dataRestRequestBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, GenericParameterBuilder parameterBuilder,
				RequestBodyBuilder requestBodyBuilder, AbstractRequestBuilder requestBuilder) {
			return new DataRestRequestBuilder(localSpringDocParameterNameDiscoverer, parameterBuilder,
					requestBodyBuilder, requestBuilder);
		}

		@Bean
		@ConditionalOnMissingBean
		DataRestResponseBuilder dataRestResponseBuilder(GenericResponseBuilder genericResponseBuilder) {
			return new DataRestResponseBuilder(genericResponseBuilder);
		}

		@Bean
		@ConditionalOnMissingBean
		DataRestTagsBuilder dataRestTagsBuilder(OpenAPIBuilder openAPIBuilder){
			return new DataRestTagsBuilder(openAPIBuilder);
		}
	}

}
