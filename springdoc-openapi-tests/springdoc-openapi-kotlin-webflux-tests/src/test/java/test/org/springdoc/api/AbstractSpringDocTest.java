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

package test.org.springdoc.api;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@WebFluxTest
@ActiveProfiles("test")
@TestPropertySource(properties =  "springdoc.api-docs.version=openapi_3_0" )
public abstract class AbstractSpringDocTest {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringDocTest.class);

	@Autowired
	private WebTestClient webTestClient;

	public static String getContent(String fileName) {
		try {
			Path path = Paths.get(AbstractSpringDocTest.class.getClassLoader().getResource(fileName).toURI());
			byte[] fileBytes = Files.readAllBytes(path);
			return new String(fileBytes, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to read file: " + fileName, e);
		}
	}

	@Test
	void testApp() throws Exception {
		String result = null;
		try {
			EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
					.expectStatus().isOk().expectBody().returnResult();

			result = new String(getResult.getResponseBody());
			String className = getClass().getSimpleName();
			String testNumber = className.replaceAll("[^0-9]", "");
			String expected = getContent("results/app" + testNumber + ".json");
			assertEquals(expected, result, true);
		}
		catch (java.lang.AssertionError e) {
			LOGGER.error(result);
			throw e;
		}
	}
}