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

package test.org.springdoc.webflux.scalar.app11;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webflux.scalar.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.containsString;
import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;
import static org.springdoc.scalar.ScalarConstants.SCALAR_JS_FILENAME;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@TestPropertySource(properties = "server.forward-headers-strategy=framework")
public class SpringDocApp11Test extends AbstractCommonTest {

	private static final String X_FORWARD_PREFIX = "/path/prefix";

	@Test
	void checkContent() {
		checkContent(SCALAR_DEFAULT_PATH);
	}

	@Test
	void checkContentForwardedPrefix() {
		Map<String, String> headers = Map.of("X-Forwarded-Prefix", X_FORWARD_PREFIX);
		checkContent(SCALAR_DEFAULT_PATH, headers, "results/app11-1");
	}

	@Test
	void checkContentForwardedPrefixHostProto() throws Exception {
		Map<String, String> headers = Map.of("X-Forwarded-Prefix", X_FORWARD_PREFIX,
				"X-Forwarded-Proto", "https", "X-Forwarded-Host", "proxy-host");
		checkContent(SCALAR_DEFAULT_PATH, headers, "results/app11-2");
	}

	@Test
	void concurrentScalarChecks() {
		CompletableFuture<?>[] tasks = IntStream.range(0, 10)
				.mapToObj(i -> CompletableFuture.runAsync(() -> {
					String prefix = X_FORWARD_PREFIX + i;

					webTestClient.get()
							.uri(SCALAR_DEFAULT_PATH)
							.header("X-Forwarded-Prefix", prefix)
							.exchange()
							.expectStatus().isOk()
							.expectBody(String.class)
							.value(containsString(prefix + SCALAR_DEFAULT_PATH))
							.value(containsString(prefix + SCALAR_DEFAULT_PATH
									+ DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME));
				}))
				.toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(tasks).join();
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
