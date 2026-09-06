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

package org.springdoc.core.customizers;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import org.springframework.core.annotation.Order;

import static org.assertj.core.api.Assertions.assertThat;

class SpringDocCustomizersTest {

	@Test
	void openApiCustomizersStreamHonorsOrderAnnotation() {
		OpenApiCustomizer lastCustomizer = new LastOpenApiCustomizer();
		OpenApiCustomizer firstCustomizer = new FirstOpenApiCustomizer();
		Set<OpenApiCustomizer> unorderedCustomizers = new LinkedHashSet<>();
		unorderedCustomizers.add(lastCustomizer);
		unorderedCustomizers.add(firstCustomizer);

		SpringDocCustomizers springDocCustomizers = new SpringDocCustomizers(
				Optional.of(unorderedCustomizers),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty());

		assertThat(springDocCustomizers.getOpenApiCustomizersStream().toList()).containsExactly(firstCustomizer, lastCustomizer);
	}

	@Test
	void globalOpenApiCustomizersStreamHonorsOrderAnnotation() {
		GlobalOpenApiCustomizer lastCustomizer = new LastGlobalOpenApiCustomizer();
		GlobalOpenApiCustomizer firstCustomizer = new FirstGlobalOpenApiCustomizer();
		Set<GlobalOpenApiCustomizer> unorderedCustomizers = new LinkedHashSet<>();
		unorderedCustomizers.add(lastCustomizer);
		unorderedCustomizers.add(firstCustomizer);

		SpringDocCustomizers springDocCustomizers = new SpringDocCustomizers(
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.of(unorderedCustomizers),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty());

		assertThat(springDocCustomizers.getGlobalOpenApiCustomizersStream().toList()).containsExactly(firstCustomizer, lastCustomizer);
	}

	@Order(1)
	private static class FirstOpenApiCustomizer implements OpenApiCustomizer {

		@Override
		public void customise(OpenAPI openApi) {
		}
	}

	@Order(2)
	private static class LastOpenApiCustomizer implements OpenApiCustomizer {

		@Override
		public void customise(OpenAPI openApi) {
		}
	}

	@Order(1)
	private static class FirstGlobalOpenApiCustomizer implements GlobalOpenApiCustomizer {

		@Override
		public void customise(OpenAPI openApi) {
		}
	}

	@Order(2)
	private static class LastGlobalOpenApiCustomizer implements GlobalOpenApiCustomizer {

		@Override
		public void customise(OpenAPI openApi) {
		}
	}
}
