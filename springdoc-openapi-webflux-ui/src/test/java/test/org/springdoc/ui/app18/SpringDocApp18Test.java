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

package test.org.springdoc.ui.app18;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import test.org.springdoc.ui.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "spring.webflux.base-path=/test",
				"server.port=9018",
				"springdoc.swagger-ui.path=/documentation/swagger-ui.html",
				"springdoc.api-docs.path=/documentation/v3/api-docs",
				"springdoc.webjars.prefix= /webjars-pref" })
class SpringDocApp18Test extends AbstractCommonTest {

	@LocalServerPort
	private int port;

	private WebClient webClient;

	@SpringBootApplication
	static class SpringDocTestApp {}

	@PostConstruct
	void init(){
		webClient =	WebClient.builder().baseUrl("http://localhost:"+port)
				.build();
	}

	@Test
	public void testIndex() throws Exception {
		HttpStatus httpStatusMono = webClient.get().uri("/test/documentation/swagger-ui.html")
				.exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode())).block();
		assertThat(httpStatusMono).isEqualTo(HttpStatus.FOUND);

		httpStatusMono = webClient.get().uri("/test/documentation/webjars-pref/swagger-ui/index.html")
				.exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode())).block();
		assertThat(httpStatusMono).isEqualTo(HttpStatus.OK);

		String contentAsString  = webClient.get().uri("/test/documentation/v3/api-docs/swagger-config").retrieve()
				.bodyToMono(String.class).block();
		String expected = getContent("results/app18-1.json");
		assertEquals(expected, contentAsString, true);
	}



}