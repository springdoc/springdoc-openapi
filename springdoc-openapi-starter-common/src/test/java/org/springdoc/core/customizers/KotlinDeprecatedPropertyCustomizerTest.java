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

package org.springdoc.core.customizers;

import java.util.Collections;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.Test;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link KotlinDeprecatedPropertyCustomizer}.
 *
 * @author springdoc
 */
class KotlinDeprecatedPropertyCustomizerTest {

	@Test
	void resolveShouldGracefullyHandleNullPointerFromNextConverter() {
		ObjectMapperProvider objectMapperProvider = mock(ObjectMapperProvider.class);
		when(objectMapperProvider.jsonMapper()).thenReturn(new ObjectMapper());

		ModelConverter nextConverter = mock(ModelConverter.class);
		when(nextConverter.resolve(any(), any(), any())).thenThrow(new NullPointerException("subtypeModel"));

		KotlinDeprecatedPropertyCustomizer customizer = new KotlinDeprecatedPropertyCustomizer(objectMapperProvider);
		ModelConverterContext context = mock(ModelConverterContext.class);
		AnnotatedType type = new AnnotatedType().type(String.class);
		Iterator<ModelConverter> chain = Collections.singletonList(nextConverter).iterator();

		Schema<?> schema = assertDoesNotThrow(() -> customizer.resolve(type, context, chain));
		assertNull(schema);
	}
}
