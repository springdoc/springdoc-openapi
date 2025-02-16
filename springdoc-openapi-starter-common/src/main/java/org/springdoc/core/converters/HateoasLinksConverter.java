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

package org.springdoc.core.converters;


import java.util.Iterator;
import java.util.Optional;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.hateoas.RepresentationModel;

/**
 * The type Hateoas links converter.
 *
 * @author bnasslahsen
 */
public class HateoasLinksConverter implements ModelConverter {

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Hateoas links converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public HateoasLinksConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	@Override
	public Schema<?> resolve(
			io.swagger.v3.core.converter.AnnotatedType type,
			ModelConverterContext context,
			Iterator<ModelConverter> chain
	) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null && RepresentationModel.class.isAssignableFrom(javaType.getRawClass())) {
			Schema<?> schema = chain.next().resolve(type, context, chain);
			if (schema != null) {
				String schemaName = Optional.ofNullable(schema.get$ref())
						.filter(ref -> ref.startsWith(Components.COMPONENTS_SCHEMAS_REF))
						.map(ref -> ref.substring(Components.COMPONENTS_SCHEMAS_REF.length()))
						.orElse(schema.getName());
				if(schemaName != null) {
					Schema original = context.getDefinedModels().get(schemaName);
					if (original == null || original.getProperties() == null) {
						return schema;
					}
					Object links = original.getProperties().get("_links");
					if (links instanceof JsonSchema jsonSchema) {
						jsonSchema.set$ref(AnnotationsUtils.COMPONENTS_REF + "Links");
						jsonSchema.setType(null);
						jsonSchema.setItems(null);
						jsonSchema.setTypes(null);
					}
					else if (links instanceof ArraySchema arraySchema) {
						arraySchema.set$ref(AnnotationsUtils.COMPONENTS_REF + "Links");
					}
				}
			}
			return schema;
		}
		return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
	}

}
