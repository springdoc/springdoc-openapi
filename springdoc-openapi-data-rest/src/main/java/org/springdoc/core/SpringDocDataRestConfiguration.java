/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.converters.Pageable;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.converters.RepresentationModelLinksOASMixin;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocDataRestConfiguration {

	static {
		getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class, Pageable.class)
		.replaceWithClass(org.springframework.data.domain.PageRequest.class,Pageable.class);
	}

	@Configuration
	@ConditionalOnClass(RepositoryRestConfiguration.class)
	class HalProviderConfiguration {

		@Bean
		public HalProvider halProvider(RepositoryRestConfiguration repositoryRestConfiguration) {
			return new HalProvider(repositoryRestConfiguration);
		}

		/**
		 * Registers an OpenApiCustomiser and a jackson mixin to ensure the definition of `Links` matches the serialized
		 * output. This is done because the customer serializer converts the data to a map before serializing it.
		 *
		 * @see org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalLinkListSerializer#serialize(Links, JsonGenerator, SerializerProvider)
		 */
		@Bean
		public OpenApiCustomiser linksSchemaCustomiser(RepositoryRestConfiguration repositoryRestConfiguration) {
			if (!repositoryRestConfiguration.useHalAsDefaultJsonMediaType()) {
				return openApi -> {};
			}
			Json.mapper().addMixIn(RepresentationModel.class, RepresentationModelLinksOASMixin.class);

			ResolvedSchema resolvedLinkSchema = ModelConverters.getInstance()
					.resolveAsResolvedSchema(new AnnotatedType(Link.class));

			return openApi -> openApi
					.schema("Link", resolvedLinkSchema.schema)
					.schema("Links", new MapSchema()
							.additionalProperties(new StringSchema())
							.additionalProperties(new ObjectSchema().$ref("#/components/schemas/Link")));
		}
	}
}
