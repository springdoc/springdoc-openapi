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

package test.org.springdoc.api.v31.app157;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
class StringyConverter implements ModelConverter {

	@Autowired
	ObjectMapperProvider objectMapperProvider;

	public StringyConverter(ObjectMapperProvider objectMapperProvider) {
		this.objectMapperProvider = objectMapperProvider;
	}


	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context,
			Iterator<ModelConverter> chain) {

		JavaType javaType = objectMapperProvider.jsonMapper().constructType(type.getType());

		if (javaType.getRawClass().equals(String.class)) {
			type.getParent().addRequiredItem("stringy");
		}
		return chain.next().resolve(type, context, chain);
	}
}
