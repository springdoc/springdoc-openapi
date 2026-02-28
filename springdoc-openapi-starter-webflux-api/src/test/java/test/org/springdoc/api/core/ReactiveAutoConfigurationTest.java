/*
 *
 *  * Copyright 2019-2026 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.core;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveAutoConfigurationTest {

	private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
			.withUserConfiguration(TestApp.class);

	@Test
	void configurations_successfully_loaded() {
		contextRunner
				.run(context -> assertThat(context)
						.hasNotFailed()
						.hasBean("openApiResource"));
	}

	@Test
	void configurations_not_loaded_when_application_is_not_web() {
		new ApplicationContextRunner()
				.withUserConfiguration(TestApp.class)
				.run(context -> assertThat(context)
						.hasNotFailed()
						.doesNotHaveBean("openApiResource"));
	}

	@Test
	void configurations_not_loaded_when_disabled() {
		contextRunner
				.withPropertyValues("springdoc.api-docs.enabled=false")
				.run(context -> assertThat(context)
						.hasNotFailed()
						.doesNotHaveBean("openApiResource"));
	}

	@Test
	void configurations_not_loaded_when_reactor_is_not_on_class_path() {
		contextRunner
				.withClassLoader(new FilteredClassLoader("org.springframework.web.reactive.HandlerResult"))
				.run(context -> assertThat(context)
						.hasNotFailed()
						.doesNotHaveBean("openApiResource"));

	}

	@SpringBootApplication
	static class TestApp {
	}
}