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

package org.springdoc.core.customizers;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.hateoas.Link;

/**
 * The type Open api hateoas links customiser.
 *
 * @author bnasslahsen
 */
public class OpenApiHateoasLinksCustomizer extends SpecFilter implements GlobalOpenApiCustomizer {

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * Instantiates a new Open api hateoas links customiser.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public OpenApiHateoasLinksCustomizer(SpringDocConfigProperties springDocConfigProperties) {
		this.springDocConfigProperties = springDocConfigProperties;
	}

	@Override
	public void customise(OpenAPI openApi) {
		ResolvedSchema resolvedLinkSchema = ModelConverters.getInstance(springDocConfigProperties.isOpenapi31())
				.resolveAsResolvedSchema(new AnnotatedType(Link.class));
		openApi
				.schema("Link", resolvedLinkSchema.schema)
				.schema("Links", new MapSchema()
						.additionalProperties(new StringSchema())
						.additionalProperties(new Schema<>().$ref(AnnotationsUtils.COMPONENTS_REF + "Link")));
		if (springDocConfigProperties.isRemoveBrokenReferenceDefinitions())
			this.removeBrokenReferenceDefinitions(openApi);
	}
}
