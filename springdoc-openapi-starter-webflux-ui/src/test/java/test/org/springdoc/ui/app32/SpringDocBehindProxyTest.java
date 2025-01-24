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

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.ui.AbstractSpringDocTest;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {
		"server.forward-headers-strategy=framework"
})
@Import(SpringDocConfig.class)
public class SpringDocBehindProxyTest extends AbstractSpringDocTest {

	private static final String X_FORWARD_PREFIX = "/path/prefix";

	@Test
	void shouldServeSwaggerUIAtDefaultPath() {
        webTestClient.get().uri("/swagger-ui/index.html").exchange()
				.expectStatus().isOk();
	}

	@Test
	void shouldReturnCorrectInitializerJS() throws Exception {
		webTestClient
                .get().uri("/swagger-ui/swagger-initializer.js")
				.header("X-Forwarded-Prefix", X_FORWARD_PREFIX)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.consumeWith(response ->
						assertThat(response.getResponseBody())
								.contains("\"configUrl\" : \"/path/prefix/v3/api-docs/swagger-config\",")
				);
	}

	@Test
	void shouldCalculateOauthRedirectBehindProxy() throws Exception {
		webTestClient
				.get().uri("/v3/api-docs/swagger-config")
				.header("X-Forwarded-Proto", "https")
				.header("X-Forwarded-Host", "proxy-host")
				.header("X-Forwarded-Prefix", X_FORWARD_PREFIX)
				.exchange()
				.expectStatus().isOk().expectBody()
                .jsonPath("$.oauth2RedirectUrl").isEqualTo("https://proxy-host/path/prefix/swagger-ui/oauth2-redirect.html");
	}

	@Test
	void shouldCalculateUrlsBehindProxy() throws Exception {
		webTestClient
				.get().uri("/v3/api-docs/swagger-config")
				.header("X-Forwarded-Prefix", X_FORWARD_PREFIX)
				.exchange()
				.expectStatus().isOk().expectBody()
				.jsonPath("$.url")
				.isEqualTo("/path/prefix/v3/api-docs")
				.jsonPath("$.configUrl")
				.isEqualTo("/path/prefix/v3/api-docs/swagger-config");
	}

	@Test
	void shouldReturnCorrectInitializerJSWhenChangingForwardedPrefixHeader() throws Exception {
		var tasks = IntStream.range(0, 10).mapToObj(i -> CompletableFuture.runAsync(() -> {
			try {
                webTestClient.get().uri("/swagger-ui/swagger-initializer.js")
						.header("X-Forwarded-Prefix", "/path/prefix" + i)
						.exchange()
						.expectStatus().isOk()
						.expectBody(String.class)
						.consumeWith(response ->
								assertThat(response.getResponseBody())
										.contains("\"configUrl\" : \"/path/prefix" + i + "/v3/api-docs/swagger-config\",")
						);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		})).toArray(CompletableFuture<?>[]::new);

		CompletableFuture.allOf(tasks).join();
	}

	@Test
	void shouldCalculateUrlsBehindProxyWhenChangingForwardedPrefixHeader() {
		var tasks = IntStream.range(0, 10).mapToObj(i -> CompletableFuture.runAsync(() -> {
			try {
				webTestClient.get().uri("/v3/api-docs/swagger-config")
						.header("X-Forwarded-Prefix", "/path/prefix" + i)
						.exchange()
						.expectStatus().isOk().expectBody()
						.jsonPath("$.url").isEqualTo("/path/prefix" + i + "/v3/api-docs")
						.jsonPath("$.configUrl").isEqualTo("/path/prefix" + i + "/v3/api-docs/swagger-config");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		})).toArray(CompletableFuture<?>[]::new);

		CompletableFuture.allOf(tasks).join();
	}

	
	@SpringBootApplication
	static class SpringDocTestApp {}
}
