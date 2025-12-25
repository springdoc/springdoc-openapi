/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 *
 */

package test.org.springdoc.ui.app35;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.CacheControl;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(properties = {
		"spring.webflux.webjars-path-pattern=/webjars-pref/**",
		"springdoc.swagger-ui.disable-swagger-default-url=true",
		"springdoc.swagger-ui.path=/documentation/swagger-ui.html"
})
public class SpringDocApp35Test extends AbstractSpringDocTest {

	@Test
	void testWebJarPrefix() {
		webTestClient.get().uri("/webjars/swagger-ui/swagger-initializer.js")
				.exchange()
				.expectStatus().isNotFound();

		EntityExchangeResult<byte[]> getResult = webTestClient.get().uri("/webjars-pref/swagger-ui/swagger-initializer.js")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().cacheControl(CacheControl.noStore())
				.expectBody().returnResult();

		var responseContent = new String(getResult.getResponseBody());
		assertFalse(responseContent.contains("https://petstore.swagger.io/v2/swagger.json"));
		assertTrue(responseContent.contains("/v3/api-docs"));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
