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

package test.org.springdoc.webmvc.scalar.shared;

import com.scalar.maven.core.ScalarProperties;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.webmvc.scalar.ScalarWebMvcController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Checks that rendering the Scalar page keeps the URL and the group sources it derives from
 * the current request out of the controller's {@link ScalarProperties}, which every request
 * shares. Groups are configured on purpose: that is the path that used to leave the shared
 * URL set to {@code null}, so that a concurrent request could render a page pointing at no
 * OpenAPI description at all.
 *
 * @author bnasslahsen
 */
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class ScalarSharedPropertiesTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ScalarWebMvcController scalarWebMvcController;

	@Test
	void doesNotMutateThePropertiesSharedByAllRequests() throws Exception {
		ScalarProperties shared = (ScalarProperties) ReflectionTestUtils.getField(scalarWebMvcController,
				"scalarProperties");
		String url = shared.getUrl();
		assertThat(shared.getSources()).isNullOrEmpty();

		mockMvc.perform(get(SCALAR_DEFAULT_PATH)).andExpect(status().isOk());

		assertThat(shared.getUrl()).isEqualTo(url);
		assertThat(shared.getSources()).isNullOrEmpty();
	}

	@Test
	void rendersTheGroupSourcesOnEveryRequest() throws Exception {
		String first = getScalarPage();
		String second = getScalarPage();

		assertThat(first).contains("http://localhost/v3/api-docs/stores", "http://localhost/v3/api-docs/pets");
		assertThat(second).isEqualTo(first);
	}

	private String getScalarPage() throws Exception {
		return mockMvc.perform(get(SCALAR_DEFAULT_PATH))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}

	@SpringBootApplication
	static class SpringDocTestApp {

		@Bean
		GroupedOpenApi storeOpenApi() {
			return GroupedOpenApi.builder().group("stores").pathsToMatch("/store/**").build();
		}

		@Bean
		GroupedOpenApi petOpenApi() {
			return GroupedOpenApi.builder().group("pets").pathsToMatch("/pet/**").build();
		}

	}

}
