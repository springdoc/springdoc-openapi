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

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@FunctionalSpringBootTest
@AutoConfigureWebTestClient(timeout = "3600000")
public abstract class AbstractSpringDocFunctionTest extends AbstractCommonTest {

	protected String groupName = "";


	@Test
	public void testApp() {
		String result = null;
		try {
			EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + groupName).exchange()
					.expectStatus().isOk().expectBody().returnResult();

			result = new String(getResult.getResponseBody());
			String className = getClass().getSimpleName();
			String testNumber = className.replaceAll("[^0-9]", "");
			String expected = getContent("results/app" + testNumber + ".json");
			assertEquals(expected, result, true);
		}
		catch (AssertionError e) {
			LOGGER.error(result);
			throw e;
		}
	}
}
