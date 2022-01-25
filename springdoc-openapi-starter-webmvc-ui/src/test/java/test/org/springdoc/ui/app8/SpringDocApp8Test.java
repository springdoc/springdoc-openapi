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

package test.org.springdoc.ui.app8;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"springdoc.swagger-ui.urls[0].name=users",
		"springdoc.swagger-ui.urls[0].url=/api-docs.yaml"
})
public class SpringDocApp8Test extends AbstractSpringDocTest {

	@Test
	public void swagger_config_for_multiple_groups() throws Exception {
		mockMvc.perform(get("/v3/api-docs/swagger-config"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("configUrl", equalTo("/v3/api-docs/swagger-config")))
				.andExpect(jsonPath("url").doesNotExist())
				.andExpect(jsonPath("urls[0].url", equalTo("/api-docs.yaml")))
				.andExpect(jsonPath("urls[0].name", equalTo("users")));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}