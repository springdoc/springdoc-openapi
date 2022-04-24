/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
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
 */

package org.springdoc.core.converters;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.converters.models.Pageable;
import org.springdoc.core.providers.ObjectMapperProvider;

/**
 * The Pageable Type models converter.
 * @author bnasslahsen
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
			if (PAGEABLE_TO_REPLACE.equals(cls.getCanonicalName()) || PAGE_REQUEST_TO_REPLACE.equals(cls.getCanonicalName())) {
				if (!type.isSchemaProperty())
					type = PAGEABLE;
				else
					type.name(cls.getSimpleName() + StringUtils.capitalize(type.getParent().getType()));
			}
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}

}