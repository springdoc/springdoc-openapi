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

package test.org.springdoc.ui.app1;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;


@TestPropertySource(properties = "springdoc.swagger-ui.layout=BaseLayout")
public class SpringDocApp1RedirectLayoutTest extends AbstractSpringDocTest {

	@Test
	public void shouldRedirectWithConfigUrlIgnoringQueryParams() {

		WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/swagger-ui.html").exchange()
				.expectStatus().isFound();
		responseSpec.expectHeader()
				.value("Location", Matchers.is("/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config&layout=BaseLayout"));

	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}