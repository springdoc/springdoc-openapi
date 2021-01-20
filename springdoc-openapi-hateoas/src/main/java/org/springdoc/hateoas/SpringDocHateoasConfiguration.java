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

package org.springdoc.hateoas;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.core.util.Json;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.hateoas.converters.CollectionModelContentConverter;
import org.springdoc.hateoas.converters.OpenApiHateoasLinksCustomiser;
import org.springdoc.hateoas.converters.RepresentationModelLinksOASMixin;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.LinkRelationProvider;

import static org.springdoc.core.Constants.LINKS_SCHEMA_CUSTOMISER;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

/**
 * The type Spring doc hateoas configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnClass(LinkRelationProvider.class)
public class SpringDocHateoasConfiguration {

	/**
	 * Hateoas hal provider hateoas hal provider.
	 *
	 * @param hateoasPropertiesOptional the hateoas properties optional
	 * @return the hateoas hal provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	HateoasHalProvider hateoasHalProvider(Optional<HateoasProperties> hateoasPropertiesOptional) {
		return new HateoasHalProvider(hateoasPropertiesOptional);
	}

	/**
	 * Collection model content converter collection model content converter.
	 *
	 * @param halProvider the hal provider
	 * @param linkRelationProvider the link relation provider
	 * @return the collection model content converter
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	CollectionModelContentConverter collectionModelContentConverter(HateoasHalProvider halProvider, LinkRelationProvider linkRelationProvider) {
		return halProvider.isHalEnabled() ? new CollectionModelContentConverter(linkRelationProvider) : null;
	}

	/**
	 * Registers an OpenApiCustomiser and a jackson mixin to ensure the definition of `Links` matches the serialized
	 * output. This is done because the customer serializer converts the data to a map before serializing it.
	 *
	 * @param halProvider the hal provider
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the open api customiser
	 * @see org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalLinkListSerializer#serialize(Links, JsonGenerator, SerializerProvider) org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalLinkListSerializer#serialize(Links, JsonGenerator, SerializerProvider)org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalLinkListSerializer#serialize(Links, JsonGenerator, SerializerProvider)org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalLinkListSerializer#serialize(Links, JsonGenerator, SerializerProvider)
	 */
	@Bean(LINKS_SCHEMA_CUSTOMISER)
	@ConditionalOnMissingBean
	@Lazy(false)
	OpenApiCustomiser linksSchemaCustomiser(HateoasHalProvider halProvider, SpringDocConfigProperties springDocConfigProperties) {
		if (!halProvider.isHalEnabled()) {
			return openApi -> {
			};
		}
		Json.mapper().addMixIn(RepresentationModel.class, RepresentationModelLinksOASMixin.class);
		return new OpenApiHateoasLinksCustomiser(springDocConfigProperties);
	}

}
