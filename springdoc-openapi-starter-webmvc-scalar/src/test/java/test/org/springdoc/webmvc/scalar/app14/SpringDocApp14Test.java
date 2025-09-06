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

package test.org.springdoc.webmvc.scalar.app14;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webmvc.scalar.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"springdoc.show-actuator=true",
				"management.server.port=9496",
				"server.servlet.context-path=/sample",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
class SpringDocApp14Test extends AbstractSpringDocActuatorTest {

	static final String ACTUATOR_BASE_PATH = "/test";
	static final String CONTEXT_PATH = "/sample";
	static final String MANAGEMENT_BASE_PATH = "/application";
	static final String REQUEST_PATH = CONTEXT_PATH  + SCALAR_DEFAULT_PATH;

	@Test
	void checkContent() throws Exception {
		checkContent( REQUEST_PATH, CONTEXT_PATH);
	}

	@Test
	void checkNotFound() throws Exception {
		try {
			actuatorRestClient.get().uri(ACTUATOR_BASE_PATH + MANAGEMENT_BASE_PATH + SCALAR_DEFAULT_PATH).retrieve().body(String.class);;
			fail("Expected 404 Not Found");
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}
	}
	
	@SpringBootApplication
	static class SpringDocTestApp {}

}