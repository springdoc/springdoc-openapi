/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.ui.app16;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import test.org.springdoc.ui.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"springdoc.use-management-port=true",
				"management.server.port=9295",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
class SpringDocApp16Test extends AbstractSpringDocActuatorTest {

	@Test
	void testIndex() {
        EntityExchangeResult<byte[]> getResult = webTestClient.get().uri("/application/swagger-ui/index.html")
				.exchange()
				.expectStatus().isOk()
				.expectBody().returnResult();
		assertThat(getResult.getResponseBody()).isNotNull();
		String contentAsString = new String(getResult.getResponseBody());
		assertThat(contentAsString).contains("Swagger UI");
	}

	@Test
	void testIndexActuator() {
		HttpStatusCode httpStatusMono = webClient.get().uri("/test/application/swagger-ui")
				.exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode())).block();
		assertThat(httpStatusMono).isEqualTo(HttpStatus.FOUND);
	}

	@Test
	void testIndexSwaggerConfig() throws Exception {
		String contentAsString = webClient.get().uri("/test/application/swagger-ui/swagger-config").retrieve()
				.bodyToMono(String.class).block();
		String expected = getContent("results/app16-1.json");
		assertEquals(expected, contentAsString, true);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}