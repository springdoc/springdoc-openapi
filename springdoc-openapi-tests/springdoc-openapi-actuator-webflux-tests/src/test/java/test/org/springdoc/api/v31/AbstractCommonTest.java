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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

/**
 * The type Abstract common test.
 */
@AutoConfigureWebTestClient(timeout = "3600000")
@ActiveProfiles("test")
@TestPropertySource(properties = { "management.endpoints.enabled-by-default=false" })
public abstract class AbstractCommonTest {

	/**
	 * The constant LOGGER.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonTest.class);

	/**
	 * The Web test client.
	 */
	@Autowired
	protected WebTestClient webTestClient;

	/**
	 * Gets content.
	 *
	 * @param fileName the file name
	 * @return the content
	 */
	protected String getContent(String fileName) {
		try {
			Path path = Paths.get(AbstractCommonTest.class.getClassLoader().getResource(fileName).toURI());
			byte[] fileBytes = Files.readAllBytes(path);
			return new String(fileBytes, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to read file: " + fileName, e);
		}
	}

	/**
	 * Test app.
	 *
	 * @param testId    the test id
	 * @param groupName the group name
	 * @throws Exception the exception
	 */
	protected void testApp(String testId, String groupName) throws Exception {
		String result = null;
		try {
			EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/" + groupName).exchange()
					.expectStatus().isOk().expectBody().returnResult();
			result = new String(getResult.getResponseBody());
			String expected = getContent("results/3.1.0/app" + testId + ".json");
			assertEquals(expected, result, true);
		}
		catch (AssertionError e) {
			LOGGER.error(result);
			throw e;
		}
	}
}
