/*
 *
 *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31.app146;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"springdoc.show-actuator=true",
				"management.server.port=9386",
				"management.endpoints.web.exposure.exclude=functions, shutdown",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp146Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() throws Exception {
		super.testApp("146-1", Constants.ACTUATOR_DEFAULT_GROUP);
	}

	@Test
	void testApp1() throws Exception {
		super.testApp("146-2", Constants.DEFAULT_GROUP_NAME);
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v31.app146" })
	static class SpringDocTestApp {}

}
