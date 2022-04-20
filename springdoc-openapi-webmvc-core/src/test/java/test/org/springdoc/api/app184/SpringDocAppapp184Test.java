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

package test.org.springdoc.api.app184;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOpenApiCustomiser;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.filters.GlobalOpenApiMethodFilter;
import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.servers.Server;
import test.org.springdoc.api.AbstractSpringDocTest;

@TestPropertySource(properties = {
		"springdoc.group-configs[0].group=group1",
		"springdoc.group-configs[0].paths-to-exclude=/group1Filtered",
})
public class SpringDocAppapp184Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {

		@Bean
		public GlobalOpenApiCustomiser addUrlGlobalBean() {
			return openApi -> openApi.getServers().add(new Server().url("urlGlobalBean"));
		}

		@Bean
		public OpenApiCustomiser addUrlBean() {
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
		public GroupedOpenApi group2(GroupedOpenApi.Builder builder) {
			return builder
					.group("group2")
					.addOpenApiCustomiser(openApi -> openApi.getServers().add(new Server().url("urlGroup2")))
					.addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(new HeaderParameter().name("headerGroup2")))
					.addOpenApiMethodFilter(method -> !Objects.equals(method.getName(), "group2Filtered"))
					.build();
		}

		@Bean
		public GroupedOpenApi group3(GroupedOpenApi.Builder builder) {
			return builder
					.group("group3")
					.addOpenApiCustomiser(openApi -> openApi.getServers().add(new Server().url("urlGroup3")))
					.addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(new HeaderParameter().name("headerGroup3")))
					.addOpenApiMethodFilter(method -> !Objects.equals(method.getName(), "group3Filtered"))
					.build();
		}

	}

	@Test
	public void testGroup1() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group1"))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/app184-1.json"), true));
	}

	@Test
	public void testGroup2() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group2"))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/app184-2.json"), true));
	}

	@Test
	public void testGroup3() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group3"))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/app184-3.json"), true));
	}

}