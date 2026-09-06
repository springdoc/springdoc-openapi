/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.utils;

import java.util.Set;

import io.swagger.v3.oas.models.media.JsonSchema;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests that an OpenAPI 3.1 {@code JsonSchema} survives a JSON clone: swagger-core writes a
 * single type as the scalar {@code type}, which has to be read back into the {@code types} set
 * instead of falling back to returning the original instance.
 *
 * @author Mattias-Sehlstedt
 */
class SpringDocUtilsTest {

	@Test
	void singleTypeForJsonSchemaJsonCloning() {
		ObjectMapperProvider provider = openapi31Provider();

		JsonSchema jsonSchema = new JsonSchema();
		jsonSchema.setTypes(Set.of("integer"));

		JsonSchema cloned = SpringDocUtils.cloneViaJson(jsonSchema, JsonSchema.class, provider.jsonMapper());

		// The object is cloned properly, we do not get the type cast fallback
		assertNotSame(jsonSchema, cloned);
		assertNotNull(cloned);
		assertEquals(Set.of("integer"), cloned.getTypes());
	}

	@Test
	void nullTypeBecomesNullTypesForJsonSchemaJsonCloning() {
		ObjectMapperProvider provider = openapi31Provider();

		JsonSchema jsonSchema = new JsonSchema();

		JsonSchema cloned = SpringDocUtils.cloneViaJson(jsonSchema, JsonSchema.class, provider.jsonMapper());

		// The object is cloned properly, we do not get the type cast fallback
		assertNotSame(jsonSchema, cloned);
		assertNotNull(cloned);
		assertNull(cloned.getTypes());
	}

	@Test
	void typeArrayIsRetainedForJsonSchemaJsonCloning() {
		ObjectMapperProvider provider = openapi31Provider();

		JsonSchema jsonSchema = new JsonSchema();
		jsonSchema.setTypes(Set.of("integer", "null"));

		JsonSchema cloned = SpringDocUtils.cloneViaJson(jsonSchema, JsonSchema.class, provider.jsonMapper());

		// The object is cloned properly, we do not get the type cast fallback
		assertNotSame(jsonSchema, cloned);
		assertNotNull(cloned);
		assertEquals(Set.of("integer", "null"), cloned.getTypes());
	}

	private ObjectMapperProvider openapi31Provider() {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		properties.getApiDocs().setVersion(OpenApiVersion.OPENAPI_3_1);
		return new ObjectMapperProvider(properties);
	}

}
