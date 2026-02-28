/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@TestPropertySource(properties = { "management.endpoints.enabled-by-default=true" })
public abstract class AbstractSpringDocActuatorTest extends AbstractCommonTest {

	protected WebClient webClient;

	@LocalManagementPort
	private int managementPort;

	@PostConstruct
	void init() {
		webClient = WebClient.builder().baseUrl("http://localhost:" + this.managementPort)
				.build();
	}

	protected void testWithWebClient(String testId, String uri) throws Exception {
		String result = null;
		try {
			result = webClient.get().uri(uri).retrieve()
					.bodyToMono(String.class).block();
			String expected = getContent("results/3.1.0/app" + testId + ".json");
			assertEquals(expected, result, true);
		}
		catch (AssertionError e) {
			LOGGER.error(result);
			throw e;
		}
	}
}
