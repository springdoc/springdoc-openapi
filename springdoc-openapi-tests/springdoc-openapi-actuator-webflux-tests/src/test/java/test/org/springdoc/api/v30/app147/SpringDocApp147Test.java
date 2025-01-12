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

package test.org.springdoc.api.v30.app147;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"springdoc.show-actuator=true",
				"management.endpoints.web.exposure.exclude=functions, shutdown",
				"management.server.port=9287",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp147Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() throws Exception {
		super.testApp("147-1", Constants.ACTUATOR_DEFAULT_GROUP);
	}

	@Test
	void testApp1() throws Exception {
		super.testApp("147-2", Constants.ACTUATOR_DEFAULT_GROUP);

	}

	@Test
	void testApp2() throws Exception {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/" + Constants.DEFAULT_GROUP_NAME)
				.exchange()
				.expectStatus().isNotFound();
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
