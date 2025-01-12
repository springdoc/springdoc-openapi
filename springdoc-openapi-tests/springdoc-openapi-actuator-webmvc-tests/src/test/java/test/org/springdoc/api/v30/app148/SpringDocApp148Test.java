/*
 *
 *  * Copyright 2019-2024 the original author or authors.
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

package test.org.springdoc.api.v30.app148;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"springdoc.show-actuator=true",
				"management.server.port=9298",
				"server.port=6276",
				"server.servlet.context-path=/toto",
				"springdoc.use-management-port=true",
				"spring.mvc.servlet.path=/titi",
				"management.endpoints.web.exposure.exclude=functions, shutdown",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp148Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() throws Exception {
		super.testWithRestTemplate("148-1","/test/application/openapi/users");
	}

	@Test
	void testApp2() throws Exception {
		super.testWithRestTemplate("148-2","/test/application/openapi/x-actuator");
	}

	@Test
	void testApp3() throws Exception {
		try {
			actuatorRestTemplate.getForObject("/test/application/openapi" + "/" + Constants.DEFAULT_GROUP_NAME, String.class);
			fail();
		}
		catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
				assertTrue(true);
		}
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
