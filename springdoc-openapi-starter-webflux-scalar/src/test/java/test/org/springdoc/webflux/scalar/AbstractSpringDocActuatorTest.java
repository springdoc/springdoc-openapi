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

package test.org.springdoc.webflux.scalar;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractSpringDocActuatorTest extends AbstractCommonTest {

	protected WebClient webClient;

	@LocalManagementPort
	private int managementPort;

	@PostConstruct
	public void init() {
		webClient = WebClient.builder().baseUrl("http://localhost:" + this.managementPort)
				.build();
		webTestClient = webTestClient.mutate().baseUrl("http://localhost")
				.build();
	}

	protected void checkContent(String requestPath) {
		String scalarJsPath = getScalarJsPath(requestPath);
		String contentAsString = webClient.get()
				.uri(requestPath)
				.accept(MediaType.TEXT_HTML)
				.retrieve()
				.bodyToMono(String.class)
				.block();

		assert contentAsString != null;
		assertTrue(contentAsString.contains(scalarJsPath));
		checkHtmlResult(contentAsString);

		HttpStatusCode status = webClient.get()
				.uri(scalarJsPath)
				.exchangeToMono(resp -> resp.releaseBody().thenReturn(resp.statusCode()))
				.block();

		assertThat(status).isEqualTo(HttpStatus.OK);
	}

	protected void checkContentWithWebTestClient(String requestPath) {
		super.checkContent(requestPath);
	}

	protected void checkHtmlResult(String htmlResult) {
		String testNumber = className.replaceAll("[^0-9]", "");
		String fileName = "results/app" + testNumber;
		checkHtmlResult(fileName, htmlResult);
	}

	protected void checkHtmlResult(String fileName, String htmlResult) {
		assertTrue(htmlResult.contains("Scalar API Reference"));
		assertEquals(this.getContent(fileName), htmlResult.replace("\r", ""));
	}
}
