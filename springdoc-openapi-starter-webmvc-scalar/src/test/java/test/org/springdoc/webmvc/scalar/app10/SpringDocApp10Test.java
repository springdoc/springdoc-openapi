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

package test.org.springdoc.webmvc.scalar.app10;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webmvc.scalar.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*", "springdoc.use-management-port=true",
				"management.server.port=9492", "management.endpoints.web.base-path="+ SpringDocApp10Test.ACTUATOR_BASE_PATH })
class SpringDocApp10Test extends AbstractSpringDocActuatorTest {

	static final String ACTUATOR_BASE_PATH = "/application";
	static final String REQUEST_PATH = ACTUATOR_BASE_PATH  + SCALAR_DEFAULT_PATH;

	@Test
	void checkNotFound() throws Exception {
		mockMvc.perform(get(SCALAR_DEFAULT_PATH))
				.andExpect(status().isNotFound());
	}

	@Test
	void checkContent() throws Exception {
		super.checkContent(REQUEST_PATH);
	}


	@SpringBootApplication
	static class SpringDocTestApp {}

}