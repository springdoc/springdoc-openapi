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

package test.org.springdoc.api.v30;

import java.net.http.HttpClient;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.server.test.LocalManagementPort;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@TestPropertySource(properties = { "management.endpoints.enabled-by-default=true" })
public abstract class AbstractSpringDocActuatorTest extends AbstractCommonTest {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonTest.class);

	protected RestClient actuatorClient;

	@LocalManagementPort
	private int managementPort;

	@PostConstruct
	void init() {
		HttpClient jdkClient = HttpClient.newBuilder()
				.followRedirects(HttpClient.Redirect.NORMAL)
				.build();
		this.actuatorClient = RestClient.builder()
				.requestFactory(new JdkClientHttpRequestFactory(jdkClient))
				.baseUrl("http://localhost:" + managementPort)
				.build();
	}

	protected void testWithRestTemplate(String testId, String uri) throws Exception {
		String result = null;
		try {
			result = actuatorClient.get()
					.uri(uri)
					.retrieve()
					.body(String.class);
			assertNotNull(result, "Response body was null for URI: " + uri);
			String expected = getContent("results/3.0.1/app" + testId + ".json");
			assertEquals(expected, result, true);
		} catch (AssertionError e) {
			LOGGER.error("JSON response for [{}]:\n{}", uri, result);
			throw e;
		}
	}

}
