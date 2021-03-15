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

package test.org.springdoc.ui.app4;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"server.forward-headers-strategy=framework", "springdoc.cache.disabled=true"})
public class SpringDocOauthRedirectUrlRecalculateTest extends AbstractSpringDocTest {

	@Test
	public void oauth2_redirect_url_recalculation() throws Exception {

		webTestClient.get().uri("/v3/api-docs/swagger-config")
				.header("X-Forwarded-Proto", "https")
				.header("X-Forwarded-Host", "host1")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.oauth2RedirectUrl").isEqualTo("https://host1/webjars/swagger-ui/oauth2-redirect.html");


		webTestClient.get().uri("/v3/api-docs/swagger-config")
				.header("X-Forwarded-Proto", "http")
				.header("X-Forwarded-Host", "host2:8080")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.oauth2RedirectUrl").isEqualTo("http://host2:8080/webjars/swagger-ui/oauth2-redirect.html");

	}

	@SpringBootApplication
	static class SpringDocTestApp {
	}

}