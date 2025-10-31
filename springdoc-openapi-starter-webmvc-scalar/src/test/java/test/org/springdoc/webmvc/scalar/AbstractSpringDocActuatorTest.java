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

package test.org.springdoc.webmvc.scalar;



import java.net.http.HttpClient;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractSpringDocActuatorTest extends AbstractCommonTest {

	protected RestClient actuatorRestClient;

	@LocalManagementPort
	private int managementPort;

	@PostConstruct
	void init() {
		HttpClient jdkClient = HttpClient.newBuilder()
				.followRedirects(HttpClient.Redirect.NORMAL)
				.build();
		this.actuatorRestClient = RestClient.builder()
				.requestFactory(new JdkClientHttpRequestFactory(jdkClient))
				.baseUrl("http://localhost:" + managementPort)
				.build();
	}

	protected void checkContent(String requestPath) throws Exception {
		String scalarJsPath = getScalarJsPath(requestPath);
		String contentAsString = actuatorRestClient.get().uri(requestPath).retrieve().body(String.class);
		assert contentAsString != null;
		assertTrue(contentAsString.contains(scalarJsPath));
		checkHtmlResult(contentAsString);
		HttpStatusCode status = actuatorRestClient.get()
				.uri(scalarJsPath)
				.retrieve()
				.toBodilessEntity()
				.getStatusCode();
		assertThat(status).isEqualTo(HttpStatus.OK);
	}

	protected void checkHtmlResult(String htmlResult) {
		String testNumber = className.replaceAll("[^0-9]", "");
		String fileName = "results/app" + testNumber;
		checkHtmlResult( fileName, htmlResult);
	}

	protected void checkHtmlResult(String fileName, String htmlResult) {
		assertTrue(htmlResult.contains("Scalar API Reference"));
		assertEquals(this.getContent(fileName), htmlResult.replace("\r", ""));
	}
	
}
