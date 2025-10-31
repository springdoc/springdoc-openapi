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

package test.org.springdoc.api.v31;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;

import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.boot.webtestclient.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@WebFluxTest
@AutoConfigureWebTestClient(timeout = "3600000")
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

	public static final HandlerFunction<ServerResponse> HANDLER_FUNCTION = request -> ServerResponse.ok().build();

	protected String groupName = "";


	@Test
	void testApp() throws Exception {
		String result = null;
		try {
			EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + groupName).exchange()
					.expectStatus().isOk().expectBody().returnResult();

			result = new String(getResult.getResponseBody());
			String className = getClass().getSimpleName();
			String testNumber = className.replaceAll("[^0-9]", "");
			String expected = getContent("results/3.1.0/app" + testNumber + ".json");
			assertEquals(expected, result, true);
		}
		catch (AssertionError e) {
			LOGGER.error(result);
			throw e;
		}
	}
}
