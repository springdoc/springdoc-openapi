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

import java.lang.reflect.Type;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.core.ResolvableType;
import org.springframework.data.web.PagedModel;

import static org.springdoc.core.utils.SpringDocUtils.getParentTypeName;

/**
 * The Spring Data Page type model converter.
 *
 * @author Claudio Nave
 */
public class PageOpenAPIConverter implements ModelConverter {

	/**
	 * The constant PAGE_TO_REPLACE.
	 */
	private static final String PAGE_TO_REPLACE = "org.springframework.data.domain.Page";

	/**
	 * The constant PAGED_MODEL.
	 */
	private static final AnnotatedType PAGED_MODEL = new AnnotatedType(PagedModel.class).resolveAsRef(true);

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Flag to replace Page with PagedModel or not.
	 */
	private final boolean replacePageWithPagedModel;

	/**
	 * Instantiates a new Page open api converter.
	 *
	 * @param replacePageWithPagedModel flag to replace Page with PagedModel or not
	 * @param springDocObjectMapper     the spring doc object mapper
	 */
	public PageOpenAPIConverter(boolean replacePageWithPagedModel, ObjectMapperProvider springDocObjectMapper) {
		this.replacePageWithPagedModel = replacePageWithPagedModel;
		this.springDocObjectMapper = springDocObjectMapper;
	}

	/**
	 * Resolve schema.
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
			if (replacePageWithPagedModel && PAGE_TO_REPLACE.equals(cls.getCanonicalName())) {
				if (!type.isSchemaProperty())
					type = resolvePagedModelType(javaType);
				else
					type.name(getParentTypeName(type, cls));
			}
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}

	/**
	 * Resolve paged model type annotated type.
	 *
	 * @param type the type
	 * @return the annotated type
	 */
	private AnnotatedType resolvePagedModelType(JavaType type) {
		if(type.hasGenericTypes()){
			JavaType innerType = type.containedType(0);
			Type pagedModelType = ResolvableType
					.forClassWithGenerics(PagedModel.class, ResolvableType.forType(innerType))
					.getType();
			return new AnnotatedType(pagedModelType).resolveAsRef(true);
		}
		else {
			return PAGED_MODEL;
		}
	}

}