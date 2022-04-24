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
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.springdoc.core.converters.ConverterUtils.isFluxTypeWrapper;
import static org.springdoc.core.converters.ConverterUtils.isResponseTypeToIgnore;
import static org.springdoc.core.converters.ConverterUtils.isResponseTypeWrapper;

/**
 * The type Response support converter.
 * @author bnasslahsen
 */
public class ResponseSupportConverter implements ModelConverter {

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Response support converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public ResponseSupportConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (isResponseTypeWrapper(cls) && !isFluxTypeWrapper(cls)) {
				JavaType innerType = javaType.getBindings().getBoundType(0);
				if (innerType == null)
					return new StringSchema();
				return context.resolve(new AnnotatedType(innerType)
						.jsonViewAnnotation(type.getJsonViewAnnotation())
						.ctxAnnotations((type.getCtxAnnotations()))
						.resolveAsRef(true));
			}
			else if (isResponseTypeToIgnore(cls))
				return null;
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}

}
