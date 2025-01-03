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

import java.util.Collection;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.util.CollectionUtils;

/**
 * Override resolved schema as there is a custom serializer that converts the data to a map before serializing it.
 *
 * @author bnasslahsen
 * @see org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer
 * @see org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer#serialize(Collection, JsonGenerator, SerializerProvider) org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer#serialize(Collection, JsonGenerator, SerializerProvider)org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer#serialize(Collection, JsonGenerator, SerializerProvider)org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer#serialize(Collection, JsonGenerator, SerializerProvider)org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer#serialize(Collection, JsonGenerator, SerializerProvider)org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer#serialize(Collection, JsonGenerator, SerializerProvider)
 */
public class CollectionModelContentConverter implements ModelConverter {

	/**
	 * The Link relation provider.
	 */
	private final LinkRelationProvider linkRelationProvider;

	/**
	 * Instantiates a new Collection model content converter.
	 *
	 * @param linkRelationProvider the link relation provider
	 */
	public CollectionModelContentConverter(LinkRelationProvider linkRelationProvider) {
		this.linkRelationProvider = linkRelationProvider;
	}

	@Override
	public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (chain.hasNext() && type != null && type.getType() instanceof CollectionType
				&& "_embedded".equalsIgnoreCase(type.getPropertyName())) {
			Schema<?> schema = chain.next().resolve(type, context, chain);
			if (schema instanceof ArraySchema) {
				Class<?> entityType = getEntityType(type);
				String entityClassName = linkRelationProvider.getCollectionResourceRelFor(entityType).value();

				return new ObjectSchema()
						.name("_embedded")
						.addProperties(entityClassName, schema);
			}
		}
		return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
	}

	/**
	 * Gets entity type.
	 *
	 * @param type the type
	 * @return the entity type
	 */
	private Class<?> getEntityType(AnnotatedType type) {
		Class<?> containerEntityType = ((CollectionType) (type.getType())).getContentType().getRawClass();
		if (EntityModel.class.isAssignableFrom(containerEntityType)) {
			TypeBindings typeBindings = ((CollectionType) type.getType()).getContentType().getBindings();
			if (!CollectionUtils.isEmpty(typeBindings.getTypeParameters()))
				return typeBindings.getBoundType(0).getRawClass();
		}
		return containerEntityType;
	}
}
