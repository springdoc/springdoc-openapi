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

package test.org.springdoc.ui.app23;

import jakarta.annotation.PostConstruct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import test.org.springdoc.ui.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "spring.webflux.base-path=/test",
				"springdoc.swagger-ui.use-root-path=true",
				"server.port=9229" })
class SpringDocApp23Test extends AbstractCommonTest {

	@LocalServerPort
	private int port;

	private WebClient webClient;

	@PostConstruct
	void init() {
		webClient = WebClient.builder().baseUrl("http://localhost:" + port)
				.build();
	}

	@Test
	void testIndex() throws Exception {
		HttpStatusCode httpStatusRoot = webClient.get().uri("/test/")
				.exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode())).block();
		Assertions.assertThat(httpStatusRoot).isEqualTo(HttpStatus.FOUND);

		HttpStatusCode httpStatusMono = webClient.get().uri("/test/swagger-ui.html")
				.exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode())).block();
		Assertions.assertThat(httpStatusMono).isEqualTo(HttpStatus.FOUND);

	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
