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

package test.org.springdoc.ui.app31;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(properties = {
		"server.forward-headers-strategy=framework",
		"springdoc.swagger-ui.path=/documentation/swagger.html"
})
public class SpringDocBehindProxyWithCustomUIPathPathTest extends AbstractSpringDocTest {

	private static final String X_FORWARD_PREFIX = "/path/prefix";

	private static final String EXTERNAL_SWAGGER_CONFIG_URL = "/path/prefix/v3/api-docs/swagger-config";
	private static final String EXTERNAL_OPENAPI_JSON_URL = "/path/prefix/v3/api-docs";

	@SpringBootApplication
	static class SpringDocTestApp {}

	@Test
	public void shouldRedirectSwaggerUIFromCustomPath() throws Exception {
		mockMvc.perform(get("/documentation/swagger.html")
						.header("X-Forwarded-Prefix", X_FORWARD_PREFIX))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", "/path/prefix/documentation/swagger-ui/index.html"));
	}

	@Test
	public void shouldCalculateUrlsBehindProxy() throws Exception {
		mockMvc.perform(get("/v3/api-docs/swagger-config")
						.header("X-Forwarded-Prefix", X_FORWARD_PREFIX))
				.andExpect(status().isOk())
				.andExpect(jsonPath("configUrl", equalTo(EXTERNAL_SWAGGER_CONFIG_URL)))
				.andExpect(jsonPath("url", equalTo(EXTERNAL_OPENAPI_JSON_URL)));
	}

	@Test
	public void shouldReturnCorrectInitializerJS() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/documentation/swagger-ui/swagger-initializer.js")
						.header("X-Forwarded-Prefix", X_FORWARD_PREFIX))
				.andExpect(status().isOk()).andReturn();
		String actualContent = mvcResult.getResponse().getContentAsString();

		assertTrue(actualContent.contains("window.ui"));
		assertTrue(actualContent.contains("\"configUrl\" : \"" + EXTERNAL_SWAGGER_CONFIG_URL + "\","));
	}
}