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

package test.org.springdoc.api.v31.app148;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"springdoc.show-actuator=true",
				"management.endpoints.web.exposure.exclude=functions, shutdown",
				"management.server.port=9388",
				"server.port=6366",
				"springdoc.use-management-port=true",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp148Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() throws Exception {
		super.testWithWebClient("148-1","/test/application/openapi/users");
	}

	@Test
	void testApp2() throws Exception {
		super.testWithWebClient("148-2","/test/application/openapi/x-actuator");
	}

	@Test
	void testApp3() throws Exception {
		try {
			webClient.get().uri("/test/application/openapi" + "/" + Constants.DEFAULT_GROUP_NAME).retrieve()
					.bodyToMono(String.class).block();
			fail();
		}
		catch (WebClientResponseException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
				assertTrue(true);
			else
				fail();
		}
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
