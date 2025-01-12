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

package test.org.springdoc.api.v31.app184;

import java.util.Objects;

import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.filters.GlobalOpenApiMethodFilter;
import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
		"springdoc.group-configs[0].group=group1",
		"springdoc.group-configs[0].paths-to-exclude=/group1Filtered",
})
public class SpringDocApp184Test extends AbstractSpringDocTest {

	@Test
	void testGroup1() throws Exception {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/group1").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app184-1.json"), true);
	}

	@Test
	void testGroup2() throws Exception {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/group2").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app184-2.json"), true);
	}

	@Test
	void testGroup3() throws Exception {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/group3").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app184-3.json"), true);
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v31.app184" })
	static class SpringDocTestApp {

		@Bean
		public GlobalOpenApiCustomizer addUrlGlobalBean() {
			return openApi -> openApi.getServers().add(new Server().url("urlGlobalBean"));
		}

		@Bean
		public OpenApiCustomizer addUrlBean() {
			return openApi -> openApi.getServers().add(new Server().url("urlBean"));
		}

		@Bean
		public GlobalOperationCustomizer addHeaderGlobaBeanl() {
			return (operation, handlerMethod) -> operation.addParametersItem(new HeaderParameter().name("headerGlobalBean"));
		}

		@Bean
		public OperationCustomizer addHeaderBean() {
			return (operation, handlerMethod) -> operation.addParametersItem(new HeaderParameter().name("headerBean"));
		}

		@Bean
		public GlobalOpenApiMethodFilter globalFilterBean() {
			return method -> !Objects.equals(method.getName(), "globalBeanFiltered");
		}

		@Bean
		public OpenApiMethodFilter filterBean() {
			return method -> !Objects.equals(method.getName(), "beanFiltered");
		}

		@Bean
		public GroupedOpenApi group2() {
			return GroupedOpenApi.builder()
					.group("group2")
					.addOpenApiCustomizer(openApi -> openApi.getServers().add(new Server().url("urlGroup2")))
					.addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(new HeaderParameter().name("headerGroup2")))
					.addOpenApiMethodFilter(method -> !Objects.equals(method.getName(), "group2Filtered"))
					.build();
		}

		@Bean
		public GroupedOpenApi group3() {
			return GroupedOpenApi.builder()
					.group("group3")
					.addOpenApiCustomizer(openApi -> openApi.getServers().add(new Server().url("urlGroup3")))
					.addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(new HeaderParameter().name("headerGroup3")))
					.addOpenApiMethodFilter(method -> !Objects.equals(method.getName(), "group3Filtered"))
					.build();
		}

	}

}