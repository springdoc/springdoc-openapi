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

package test.org.springdoc.api.v30.app72;

import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

class BlockingAutoConfigurationTest {

	private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withUserConfiguration(TestApp.class);

	@Test
	void configurations_successfully_loaded() {
		contextRunner
				.withPropertyValues("springdoc.show-actuator=true")
				.run(context -> assertThat(context)
						.hasNotFailed()
						.hasBean("openApiResource")
						.hasBean("actuatorProvider")
						.hasBean("multipleOpenApiResource")
				);
	}

	@Test
	void configurations_not_loaded_when_application_is_not_web() {
		new ApplicationContextRunner()
				.withUserConfiguration(TestApp.class)
				.run(context -> assertThat(context)
						.hasNotFailed()
						.doesNotHaveBean("openApiResource")
						.doesNotHaveBean("actuatorProvider")
						.doesNotHaveBean("multipleOpenApiResource")
				);
	}

	@Test
	void actuator_configuration_not_loaded_when_not_enabled_explicitly() {
		contextRunner
				.run(context -> assertThat(context)
						.hasNotFailed()
						.hasBean("openApiResource")
						.doesNotHaveBean("actuatorPprrovider")
						.hasBean("multipleOpenApiResource")
				);
	}

	@Test
	void configurations_not_loaded_when_disabled() {
		contextRunner
				.withPropertyValues("springdoc.api-docs.enabled=false")
				.run(context -> assertThat(context)
						.hasNotFailed()
						.doesNotHaveBean("openApiResource")
						.doesNotHaveBean("actuatorProvider")
						.doesNotHaveBean("multipleOpenApiResource")
				);
	}

	@Test
	void configurations_not_loaded_when_mvc_is_not_on_class_path() {
		contextRunner
				.withClassLoader(new FilteredClassLoader("org.springframework.web.context.support.GenericWebApplicationContext"))
				.run(context -> assertThat(context)
						.hasNotFailed()
						.doesNotHaveBean("openApiResource")
						.doesNotHaveBean("actuatorProvider")
						.doesNotHaveBean("multipleOpenApiResource")
				);

	}

	@SpringBootApplication
	static class TestApp {
		@Bean
		GroupedOpenApi testGroupedOpenApi() {
			return GroupedOpenApi.builder()
					.group("test-group")
					.packagesToScan("org.test")
					.build();
		}
	}
}