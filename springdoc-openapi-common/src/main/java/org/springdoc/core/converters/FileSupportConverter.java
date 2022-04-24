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
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.springdoc.core.GenericParameterService.isFile;

/**
 * The type File support converter.
 * @author bnasslahsen
 */
public class FileSupportConverter implements ModelConverter {

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new File support converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public FileSupportConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (isFile(cls))
				return new FileSchema();
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}

}