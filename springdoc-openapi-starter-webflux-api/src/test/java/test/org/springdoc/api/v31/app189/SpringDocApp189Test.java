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

package test.org.springdoc.api.v31.app189;

import java.time.Duration;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractCommonTest;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@WebFluxTest
public class SpringDocApp189Test extends AbstractCommonTest {

	@Test
	public void testWithDifferentLocales() throws Exception {
		runTestWithLocale("en-GB");
		runTestWithLocale("de-DE");
	}

	private void runTestWithLocale(String locale) throws JSONException {
		EntityExchangeResult<byte[]> getResult = webTestClient.mutate().responseTimeout(Duration.ofMinutes(1000)).build()
				.get().uri(Constants.DEFAULT_API_DOCS_URL)
				.header("Accept-Language", locale)
				.exchange()
				.expectStatus().isOk().expectBody().returnResult();

		String result = new String(getResult.getResponseBody());
		String className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		String expected = getContent("results/3.1.0/app" + testNumber + ".json");
		assertEquals(expected, result, true);
	}

}
