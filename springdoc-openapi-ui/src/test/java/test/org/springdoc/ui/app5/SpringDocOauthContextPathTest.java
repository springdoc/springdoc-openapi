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

package test.org.springdoc.ui.app5;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "server.servlet.context-path=/context-path")
public class SpringDocOauthContextPathTest extends AbstractSpringDocTest {

	@Test
	public void oauth2_redirect_url_calculated_with_context_path() throws Exception {
		mockMvc.perform(get("/context-path/v3/api-docs/swagger-config").contextPath("/context-path"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("oauth2RedirectUrl", equalTo("http://localhost/context-path/swagger-ui/oauth2-redirect.html")));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}