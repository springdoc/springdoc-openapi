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
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.ui.AbstractSpringDocTest;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The test to make sure no race condition issues are present when several parallel requests
 * are sent to get Swagger config.
 *
 * @author Dmitry Lebedko (lebedko.dmitrii@gmail.com)
 */
@TestPropertySource(properties = {
		"springdoc.swagger-ui.urls[0].name=first-user-list",
		"springdoc.swagger-ui.urls[0].url=/api-docs.yaml",
		"springdoc.swagger-ui.urls[1].name=second-user-list",
		"springdoc.swagger-ui.urls[1].url=/api-docs.yaml",
		"springdoc.swagger-ui.urls[2].name=third-user-list",
		"springdoc.swagger-ui.urls[2].url=/api-docs.yaml"
})
public class SpringDocApp8MultipleUrlsSeveralParallelRequestsTest extends AbstractSpringDocTest {

	private static final int PARALLEL_REQUEST_NUMBER = 100;

	/**
	 * Sends {@link SpringDocApp8MultipleUrlsSeveralParallelRequestsTest#PARALLEL_REQUEST_NUMBER} requests
	 * simultaneously to make sure no race condition issues are present.
	 */
	@Test
	public void swagger_config_for_multiple_groups_and_many_parallel_requests() {
		assertDoesNotThrow(() -> {
			allOf(Stream.generate(() -> runAsync(() -> {
						try {
							mockMvc.perform(get("/v3/api-docs/swagger-config"))
									.andExpect(status().isOk())
									.andExpect(jsonPath("configUrl", equalTo("/v3/api-docs/swagger-config")))
										.andExpect(jsonPath("url").doesNotExist())
									.andExpect(jsonPath("urls.length()", equalTo(3)))
									.andExpect(jsonPath("urls[0].url", equalTo("/api-docs.yaml")))
									.andExpect(jsonPath("urls[0].name", equalTo("first-user-list")))
									.andExpect(jsonPath("urls[1].url", equalTo("/api-docs.yaml")))
									.andExpect(jsonPath("urls[1].name", equalTo("second-user-list")))
									.andExpect(jsonPath("urls[2].url", equalTo("/api-docs.yaml")))
									.andExpect(jsonPath("urls[2].name", equalTo("third-user-list")));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}))
					.limit(PARALLEL_REQUEST_NUMBER)
					.toArray(CompletableFuture[]::new))
					.join();
		}, "Swagger config is supposed to be delivered successfully " +
				"no matter how many parallel requests are sent");
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}