/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.webflux.scalar.app5;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import test.org.springdoc.webflux.scalar.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*", "springdoc.use-management-port=true",
				"management.server.port=9593", "management.endpoints.web.base-path="+ SpringDocApp5Test.ACTUATOR_BASE_PATH })
class SpringDocApp5Test extends AbstractSpringDocActuatorTest {

	static final String ACTUATOR_BASE_PATH = "/application";

	static final String SCALAR_PATH = ACTUATOR_BASE_PATH + SCALAR_DEFAULT_PATH;

	@Test
	void checkNotFound() {
		webTestClient.get().uri(SCALAR_DEFAULT_PATH)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void checkContent() {
		HttpStatusCode httpStatusMono = webClient.get().uri("/application/scalar")
				.exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode())).block();
		assertThat(httpStatusMono).isEqualTo(HttpStatus.OK);
		checkContent(SCALAR_PATH);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
	

}