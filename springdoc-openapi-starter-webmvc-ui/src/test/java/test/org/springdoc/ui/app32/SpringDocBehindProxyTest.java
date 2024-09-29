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

package test.org.springdoc.ui.app32;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"server.forward-headers-strategy=framework"
})
public class SpringDocBehindProxyTest extends AbstractSpringDocTest {

	private static final String X_FORWARD_PREFIX = "/path/prefix";

	@Test
	public void shouldServeSwaggerUIAtDefaultPath() throws Exception {
		mockMvc.perform(get("/swagger-ui/index.html"))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnCorrectInitializerJS() throws Exception {
		mockMvc.perform(get("/swagger-ui/swagger-initializer.js")
						.header("X-Forwarded-Prefix", X_FORWARD_PREFIX))
				.andExpect(status().isOk())
				.andExpect(content().string(
						containsString("\"configUrl\" : \"/path/prefix/v3/api-docs/swagger-config\",")
				));
	}

	@Test
	public void shouldCalculateOauthRedirectBehindProxy() throws Exception {
		mockMvc.perform(get("/v3/api-docs/swagger-config")
						.header("X-Forwarded-Proto", "https")
						.header("X-Forwarded-Host", "proxy-host")
						.header("X-Forwarded-Prefix", X_FORWARD_PREFIX))
				.andExpect(status().isOk())
				.andExpect(jsonPath("oauth2RedirectUrl",
						equalTo("https://proxy-host/path/prefix/swagger-ui/oauth2-redirect.html")
				));
	}

	@Test
	public void shouldCalculateUrlsBehindProxy() throws Exception {
		mockMvc.perform(get("/v3/api-docs/swagger-config")
						.header("X-Forwarded-Prefix", X_FORWARD_PREFIX))
				.andExpect(status().isOk())
				.andExpect(jsonPath("url",
						equalTo("/path/prefix/v3/api-docs")
				))
				.andExpect(jsonPath("configUrl",
						equalTo("/path/prefix/v3/api-docs/swagger-config")
				));
	}

	@Test
	public void shouldReturnCorrectInitializerJSWhenChangingForwardedPrefixHeader() throws Exception {
		var tasks = IntStream.range(0, 10).mapToObj(i -> CompletableFuture.runAsync(() -> {
			try {
				mockMvc.perform(get("/swagger-ui/swagger-initializer.js")
								.header("X-Forwarded-Prefix", "/path/prefix" + i))
						.andExpect(status().isOk())
						.andExpect(content().string(
								containsString("\"configUrl\" : \"/path/prefix" + i + "/v3/api-docs/swagger-config\",")
						));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		})).toArray(CompletableFuture<?>[]::new);

		CompletableFuture.allOf(tasks).join();
	}

	@Test
	public void shouldCalculateUrlsBehindProxyWhenChangingForwardedPrefixHeader() {
		var tasks = IntStream.range(0, 10).mapToObj(i -> CompletableFuture.runAsync(() -> {
			try {
				mockMvc.perform(get("/v3/api-docs/swagger-config")
								.header("X-Forwarded-Prefix", X_FORWARD_PREFIX + i))
						.andExpect(status().isOk())
						.andExpect(jsonPath("url",
								equalTo("/path/prefix" + i + "/v3/api-docs")
						))
						.andExpect(jsonPath("configUrl",
								equalTo("/path/prefix" + i + "/v3/api-docs/swagger-config")
						));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		})).toArray(CompletableFuture<?>[]::new);

		CompletableFuture.allOf(tasks).join();
	}
	
	@SpringBootApplication
	static class SpringDocTestApp {}
}
