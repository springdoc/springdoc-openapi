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

package test.org.springdoc.ui.app22;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"springdoc.api-docs.enabled=false",
		"springdoc.api-docs.path=/api-docs",
		"springdoc.swagger-ui.url=/api-docs/xxx/v1/openapi.yml",
})
public class SpringDocApp22Test extends AbstractSpringDocTest {

	@Test
	void test_apidocs_disabled() throws Exception {
		mockMvc.perform(get("/api-docs/swagger-config"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("url", equalTo("/api-docs/xxx/v1/openapi.yml")))
				.andExpect(jsonPath("configUrl", equalTo("/api-docs/swagger-config")))
				.andExpect(jsonPath("validatorUrl", equalTo("")))
				.andExpect(jsonPath("oauth2RedirectUrl", equalTo("http://localhost/swagger-ui/oauth2-redirect.html")));
	}
}