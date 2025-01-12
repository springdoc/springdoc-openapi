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

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.converters.models.Sort;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.springdoc.core.utils.SpringDocUtils.getParentTypeName;

/**
 * The Spring Data Sort type model converter.
 *
 * @author daniel -shuy
 */
public class SortOpenAPIConverter implements ModelConverter {

	/**
	 * The constant SORT_TO_REPLACE.
	 */
	private static final String SORT_TO_REPLACE = "org.springframework.data.domain.Sort";

	/**
	 * The constant SORT.
	 */
	private static final AnnotatedType SORT = new AnnotatedType(Sort.class).resolveAsRef(true);

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Sort open api converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public SortOpenAPIConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	/**
	 * Resolve schema.
	 *
	 * @param type the type
	 * @param context the context
	 * @param chain the chain
	 * @return the schema
	 */
	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (SORT_TO_REPLACE.equals(cls.getCanonicalName())) {
				if (!type.isSchemaProperty())
					type = SORT;
				else
					type.name(getParentTypeName(type, cls));
			}
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}

}