/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.converters.models.Pageable;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.springdoc.core.utils.SpringDocUtils.getParentTypeName;

/**
 * The Pageable Type models converter.
 *
 * @author bnasslahsen
 * @author dpkass
 */
public class PageableOpenAPIConverter implements ModelConverter {

	/**
	 * The constant PAGEABLE_TO_REPLACE.
	 */
	private static final String PAGEABLE_TO_REPLACE = "org.springframework.data.domain.Pageable";

	/**
	 * The constant PAGE_REQUEST_TO_REPLACE.
	 */
	private static final String PAGE_REQUEST_TO_REPLACE = "org.springframework.data.domain.PageRequest";

	/**
	 * The constant PAGEABLE.
	 */
	private static final AnnotatedType PAGEABLE = new AnnotatedType(Pageable.class).resolveAsRef(true);

	/**
	 * The standard pageable response property order.
	 */
	private static final List<String> PAGEABLE_PROPERTY_ORDER = List.of("offset", "paged", "pageNumber", "pageSize", "sort", "unpaged");

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Pageable open api converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public PageableOpenAPIConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	/**
	 * Resolve schema.
	 *
	 * @param type    the type
	 * @param context the context
	 * @param chain   the chain
	 * @return the schema
	 */
	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		boolean isPageableType = false;
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			isPageableType = PAGEABLE_TO_REPLACE.equals(cls.getCanonicalName()) || PAGE_REQUEST_TO_REPLACE.equals(cls.getCanonicalName());
			if (isPageableType) {
				if (!type.isSchemaProperty())
					type = PAGEABLE;
				else
					type.name(getParentTypeName(type, cls));
			}
		}
		Schema schema = (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
		if (isPageableType)
			sortSchemaProperties(schema, context);
		return schema;
	}

	/**
	 * Sort the response schema properties.
	 *
	 * @param schema  the schema
	 * @param context the context
	 */
	private void sortSchemaProperties(Schema schema, ModelConverterContext context) {
		if (schema != null && schema.get$ref() != null && schema.get$ref().startsWith(Components.COMPONENTS_SCHEMAS_REF))
			schema = context.getDefinedModels().get(schema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length()));
		if (schema == null || schema.getProperties() == null)
			return;

		Map<String, Schema> properties = schema.getProperties();
		if (!properties.keySet().containsAll(PAGEABLE_PROPERTY_ORDER))
			return;

		Map<String, Schema> sortedProperties = new LinkedHashMap<>();
		PAGEABLE_PROPERTY_ORDER.forEach(property -> sortedProperties.put(property, properties.get(property)));
		properties.forEach(sortedProperties::putIfAbsent);
		schema.setProperties(sortedProperties);
	}

}
