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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.data.rest.converters.CollectionModelContentConverter;
import org.springdoc.data.rest.converters.Pageable;
import org.springdoc.data.rest.converters.RepresentationModelLinksOASMixin;
import org.springdoc.data.rest.customisers.QuerydslPredicateOperationCustomizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.LinkRelationProvider;

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
	@Lazy(false)
	HalProvider halProvider(Optional<RepositoryRestConfiguration> repositoryRestConfiguration) {
		return new HalProvider(repositoryRestConfiguration);
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	CollectionModelContentConverter collectionModelContentConverter(HalProvider halProvider, LinkRelationProvider linkRelationProvider) {
		return halProvider.isHalEnabled() ? new CollectionModelContentConverter(linkRelationProvider) : null;
	}

	/**
	 * Registers an OpenApiCustomiser and a jackson mixin to ensure the definition of `Links` matches the serialized
	 * output. This is done because the customer serializer converts the data to a map before serializing it.
	 *
	 * @see org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalLinkListSerializer#serialize(Links, JsonGenerator, SerializerProvider)
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	OpenApiCustomiser linksSchemaCustomiser(HalProvider halProvider) {
		if (!halProvider.isHalEnabled()) {
			return openApi -> {
			};
		}
		Json.mapper().addMixIn(RepresentationModel.class, RepresentationModelLinksOASMixin.class);

		ResolvedSchema resolvedLinkSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(new AnnotatedType(Link.class));

		return openApi -> openApi
				.schema("Link", resolvedLinkSchema.schema)
				.schema("Links", new MapSchema()
						.additionalProperties(new StringSchema())
						.additionalProperties(new ObjectSchema().$ref(AnnotationsUtils.COMPONENTS_REF +"Link")));
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
