/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.data.rest;

import java.util.Optional;

import com.querydsl.core.types.Predicate;
import org.springdoc.core.converters.models.Pageable;
import org.springdoc.data.rest.customisers.QuerydslPredicateOperationCustomizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocDataRestConfiguration {

	static {
		getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class, Pageable.class)
				.replaceWithClass(org.springframework.data.domain.PageRequest.class, Pageable.class);
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
}
