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

package test.org.springdoc.ui.app43;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"spring.mvc.servlet.path=/api",
		"springdoc.api-docs.path=/api/v3/api-docs",
		"springdoc.swagger-ui.path=/api/swagger-ui" })
public class SpringDocApp43Test extends AbstractSpringDocTest {

	@Test
	void configUrl_not_duplicated_when_servlet_path_is_part_of_api_docs_path() throws Exception {
		mockMvc.perform(get("/api/api/v3/api-docs/swagger-config").servletPath("/api"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("configUrl", equalTo("/api/v3/api-docs/swagger-config")));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
