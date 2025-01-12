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

package test.org.springdoc.api.v31.app147;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"springdoc.show-actuator=true",
				"management.server.port=9387",
				"management.endpoints.web.exposure.exclude=functions, shutdown",
				"server.servlet.context-path=/sample",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp147Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/" + Constants.ACTUATOR_DEFAULT_GROUP))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app147-1.json"), true));
	}

	@Test
	void testApp1() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app147-2.json"), true));
	}

	@Test
	void testApp2() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/" + Constants.DEFAULT_GROUP_NAME))
				.andExpect(status().isNotFound());
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
