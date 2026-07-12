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

import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringDocUtils}.
 */
class SpringDocUtilsTest {

	@Test
	void removeNullKeySchemas_removesNullKeyFromTopLevelSchemaProperties() {
		Schema<Object> parentSchema = new Schema<>();
		Map<String, Schema> props = new LinkedHashMap<>();
		props.put("name", new StringSchema());
		props.put(null, new StringSchema());
		props.put("count", new StringSchema());
		parentSchema.setProperties(props);

		OpenAPI openAPI = new OpenAPI()
				.components(new Components().addSchemas("MyDto", parentSchema));

		SpringDocUtils.removeNullKeySchemas(openAPI);

		Map<String, Schema> result = openAPI.getComponents().getSchemas().get("MyDto").getProperties();
		assertThat(result).containsOnlyKeys("name", "count");
		assertThat(result).doesNotContainKey(null);
	}

	@Test
	void removeNullKeySchemas_removesNullKeyFromNestedSchemaProperties() {
		Schema<Object> nestedSchema = new Schema<>();
		Map<String, Schema> nestedProps = new LinkedHashMap<>();
		nestedProps.put("field", new StringSchema());
		nestedProps.put(null, new StringSchema());
		nestedSchema.setProperties(nestedProps);

		Schema<Object> parentSchema = new Schema<>();
		Map<String, Schema> parentProps = new LinkedHashMap<>();
		parentProps.put("nested", nestedSchema);
		parentSchema.setProperties(parentProps);

		OpenAPI openAPI = new OpenAPI()
				.components(new Components().addSchemas("Parent", parentSchema));

		SpringDocUtils.removeNullKeySchemas(openAPI);

		Schema<?> parentResult = openAPI.getComponents().getSchemas().get("Parent");
		Schema<?> nestedResult = (Schema<?>) parentResult.getProperties().get("nested");
		assertThat(nestedResult.getProperties()).containsOnlyKeys("field");
		assertThat(nestedResult.getProperties()).doesNotContainKey(null);
	}

	@Test
	void removeNullKeySchemas_toleratesNullOpenApi() {
		SpringDocUtils.removeNullKeySchemas(null);
	}

	@Test
	void removeNullKeySchemas_toleratesNullComponents() {
		SpringDocUtils.removeNullKeySchemas(new OpenAPI());
	}

	@Test
	void removeNullKeySchemas_toleratesSchemaWithNoProperties() {
		Schema<Object> emptySchema = new Schema<>();
		OpenAPI openAPI = new OpenAPI()
				.components(new Components().addSchemas("Empty", emptySchema));
		SpringDocUtils.removeNullKeySchemas(openAPI);
		assertThat(openAPI.getComponents().getSchemas().get("Empty").getProperties()).isNull();
	}
}
