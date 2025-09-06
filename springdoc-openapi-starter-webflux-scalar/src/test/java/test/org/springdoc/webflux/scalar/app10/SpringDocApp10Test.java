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

package test.org.springdoc.webflux.scalar.app10;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import test.org.springdoc.webflux.scalar.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "spring.webflux.base-path=/test",
				"scalar.path=/",
				"server.port=9529" })
class SpringDocApp10Test extends AbstractSpringDocActuatorTest {

	@Test
	void checkContent() {
		HttpStatusCode httpStatusRoot = webClient.get().uri("/test/")
				.exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode())).block();
		Assertions.assertThat(httpStatusRoot).isEqualTo(HttpStatus.OK);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
