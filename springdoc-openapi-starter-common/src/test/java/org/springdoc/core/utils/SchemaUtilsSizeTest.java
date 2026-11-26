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

package org.springdoc.core.utils;

import java.lang.annotation.Annotation;
import java.util.List;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@code @Size} keeps the stricter of its own minimum and the one already on the schema, such as
 * the {@code minLength} or {@code minItems} of 1 that {@code @NotBlank} and {@code @NotEmpty} give.
 */
class SchemaUtilsSizeTest {

	@ParameterizedTest
	@CsvSource({ "max, 1, 1", "min, 1, 2", "max, , 0" })
	void stringMinLength(String method, Integer existing, int expected) throws NoSuchMethodException {
		Schema<?> schema = new StringSchema().minLength(existing);
		SchemaUtils.applyValidationsToSchema(schema, List.of(size(method)), OpenApiVersion.OPENAPI_3_1.getVersion());
		assertEquals(expected, schema.getMinLength());
	}

	@ParameterizedTest
	@CsvSource({ "max, 1, 1", "min, 1, 2", "max, , 0" })
	void arrayMinItems(String method, Integer existing, int expected) throws NoSuchMethodException {
		Schema<?> schema = new ArraySchema().items(new StringSchema()).minItems(existing);
		SchemaUtils.applyValidationsToSchema(schema, List.of(size(method)), OpenApiVersion.OPENAPI_3_1.getVersion());
		assertEquals(expected, schema.getMinItems());
	}

	private static Annotation size(String method) throws NoSuchMethodException {
		return SchemaUtilsSizeTest.class.getDeclaredMethod(method, String.class).getParameterAnnotations()[0][0];
	}

	private void max(@Size(max = 10) String value) {
	}

	private void min(@Size(min = 2, max = 10) String value) {
	}

}
