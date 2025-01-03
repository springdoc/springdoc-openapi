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
import com.fasterxml.jackson.databind.type.ArrayType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.reactivestreams.Publisher;
import org.springdoc.core.providers.ObjectMapperProvider;
import reactor.core.publisher.Flux;

import static org.springdoc.core.converters.ConverterUtils.isFluxTypeWrapper;
import static org.springdoc.core.converters.ConverterUtils.isResponseTypeWrapper;
import static org.springdoc.core.utils.SpringDocUtils.getConfig;


/**
 * The type Web flux support converter.
 *
 * @author bnasslahsen
 */
public class WebFluxSupportConverter implements ModelConverter {

	/**
	 * The Object mapper provider.
	 */
	private final ObjectMapperProvider objectMapperProvider;

	/**
	 * Instantiates a new Web flux support converter.
	 *
	 * @param objectMapperProvider the object mapper provider
	 */
	public WebFluxSupportConverter(ObjectMapperProvider objectMapperProvider) {
		this.objectMapperProvider = objectMapperProvider;
		getConfig().addResponseWrapperToIgnore(Publisher.class)
				.addFluxWrapperToIgnore(Flux.class);
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = objectMapperProvider.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (isFluxTypeWrapper(cls)) {
				JavaType innerType = javaType.getBindings().getBoundType(0);
				if (innerType == null)
					return new StringSchema();
				else if (innerType.getBindings() != null && isResponseTypeWrapper(innerType.getRawClass())) {
					type = new AnnotatedType(innerType).jsonViewAnnotation(type.getJsonViewAnnotation()).resolveAsRef(true);
					return this.resolve(type, context, chain);
				}
				else {
					ArrayType arrayType = ArrayType.construct(innerType, null);
					type = new AnnotatedType(arrayType).jsonViewAnnotation(type.getJsonViewAnnotation()).resolveAsRef(true);
				}
			}
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}
}