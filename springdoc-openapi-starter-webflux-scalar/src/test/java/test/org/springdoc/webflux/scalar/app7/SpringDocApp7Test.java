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

package test.org.springdoc.webflux.scalar.app7;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webflux.scalar.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "server.port=55527",
		"management.endpoints.web.exposure.include=*",
				"springdoc.show-actuator=true",
				"management.server.port=9596",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
class SpringDocApp7Test extends AbstractSpringDocActuatorTest {

	static final String ACTUATOR_BASE_PATH = "/test";
	static final String MANAGEMENT_BASE_PATH = "/application";
	static final String REQUEST_PATH = ACTUATOR_BASE_PATH + MANAGEMENT_BASE_PATH + SCALAR_DEFAULT_PATH;

	@Test
	void checkNotFound() {
		HttpStatusCode status = webClient.get()
				.uri(REQUEST_PATH)
				.exchangeToMono(resp -> resp.releaseBody().thenReturn(resp.statusCode()))
				.block();
		assertThat(status).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void checkContent() {
		checkContentWithWebTestClient(SCALAR_DEFAULT_PATH);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}