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

package test.org.springdoc.ui.app3;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;


@TestPropertySource(properties = {
		"springdoc.swagger-ui.path=/documentation/swagger-ui.html",
		"springdoc.api-docs.path=/documentation/v3/api-docs"
})
public class SpringDocApp3RedirectDefaultTest extends AbstractSpringDocTest {

	@Test
	public void shouldRedirectWithDefaultQueryParams() {
		WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/documentation/swagger-ui.html").exchange()
				.expectStatus().isFound();
		responseSpec.expectHeader()
				.value("Location", Matchers.is("/documentation/webjars/swagger-ui/index.html?configUrl=/documentation/v3/api-docs/swagger-config"));

		webTestClient.get().uri("/documentation/v3/api-docs/swagger-config").exchange()
				.expectStatus().isOk().expectBody()
				.jsonPath("$.validatorUrl").isEqualTo("")
				.jsonPath("$.oauth2RedirectUrl").isEqualTo("/documentation/webjars/swagger-ui/oauth2-redirect.html");
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}