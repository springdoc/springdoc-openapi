/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springdoc.core.providers;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springdoc.core.customizers.JavaNullablePropertyCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

/**
 * Checks that provider and swagger-core customizations use the same property names.
 *
 * @author Dmytro Shubchynskyi
 */
class ObjectMapperProviderCompatibilityTest {

	@Test
	void providerNamingStrategyAppliesToResolvedSchemas() {
		ObjectMapper sharedMapper = ObjectMapperFactory.createJson31();
		try (MockedStatic<Json31> json31 = mockStatic(Json31.class, CALLS_REAL_METHODS)) {
			json31.when(Json31::mapper).thenReturn(sharedMapper);
			ObjectMapperProvider provider = new ObjectMapperProvider(new SpringDocConfigProperties());
			provider.jsonMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

			Schema<?> schema = new ModelConverters(true)
				.resolveAsResolvedSchema(new AnnotatedType(Person.class)).schema;

			assertEquals(Set.of("first_name", "middle_name"), schema.getProperties().keySet());
		}
	}

	@Test
	void globalNamingStrategyPreservesNullableProperties() {
		ObjectMapper sharedMapper = ObjectMapperFactory.createJson31();
		try (MockedStatic<Json31> json31 = mockStatic(Json31.class, CALLS_REAL_METHODS)) {
			json31.when(Json31::mapper).thenReturn(sharedMapper);
			ObjectMapperProvider provider = new ObjectMapperProvider(new SpringDocConfigProperties());
			Json31.mapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
			ModelConverters converters = new ModelConverters(true);
			converters.addConverter(new JavaNullablePropertyCustomizer(provider));

			Schema<?> schema = converters.resolveAsResolvedSchema(new AnnotatedType(Person.class)).schema;

			assertEquals(Set.of("string", "null"), schema.getProperties().get("middle_name").getTypes());
		}
	}

	static class Person {

		public String firstName;

		public @Nullable String middleName;

	}

}
