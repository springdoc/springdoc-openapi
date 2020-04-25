/*
 *
 *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.api.app72;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheAutoConfigurationTest1 {

	private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withUserConfiguration(TestApp.class);

	@Test
	public void cache_configuration_loaded_when_not_disabled_explicitly() {
		contextRunner
				.run(context -> assertThat(context)
						.hasNotFailed()
						.hasBean("openApiResource")
						.doesNotHaveBean("springdocBeanFactoryPostProcessor")
						.doesNotHaveBean("multipleOpenApiResource")
				);
	}

	@Test
	public void cache_configuration_loaded_when_disabled_explicitly() {
		contextRunner
				.withPropertyValues("springdoc.cache.disabled=false")
				.run(context -> assertThat(context)
						.hasNotFailed()
						.hasBean("openApiResource")
						.doesNotHaveBean("springdocBeanFactoryPostProcessor")
						.doesNotHaveBean("multipleOpenApiResource")
				);
	}

	@Test
	public void cache_configurations_successfully_disabled() {
		contextRunner
				.withPropertyValues("springdoc.cache.disabled=true")
				.run(context -> assertThat(context)
						.hasNotFailed()
						.hasBean("openApiResource")
						.hasBean("springdocBeanFactoryPostProcessor")
						.doesNotHaveBean("multipleOpenApiResource")
				);
	}

	@Test
	public void group_configuration_loaded() {
		contextRunner
				.withPropertyValues("springdoc.group-configs[0].group=stores", "springdoc.group-configs[0].paths-to-match=/store/**")
				.run(context -> assertThat(context)
						.hasNotFailed()
						.hasBean("openApiResource")
						.hasBean("multipleOpenApiResource")
						.hasBean("springdocBeanFactoryPostProcessor")
				);
	}


	@EnableAutoConfiguration
	static class TestApp {

	}
}