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

package test.org.springdoc.webmvc.scalar.app4;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webmvc.scalar.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "spring.jackson.property-naming-strategy=UPPER_CAMEL_CASE", "springdoc.show-actuator=true",
				"management.endpoints.web.base-path=/management",
				"server.servlet.context-path="+ SpringDocApp4Test.CONTEXT_PATH, "management.server.port=9401", "management.server.base-path="+ SpringDocApp4Test.CONTEXT_PATH })
public class SpringDocApp4Test extends AbstractSpringDocActuatorTest {

	static final String CONTEXT_PATH = "/demo/api";
	static final String REQUEST_PATH = CONTEXT_PATH  + SCALAR_DEFAULT_PATH;
	
	@Test
	void checkContent() throws Exception {
		checkContent(REQUEST_PATH, CONTEXT_PATH);
	}
	
	@Test
	void checkApi() throws Exception {
		mockMvc.perform(get("/demo/api/v3/api-docs/springdocDefault").contextPath("/demo/api"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/demo/api/v3/api-docs/x-actuator").contextPath("/demo/api"))
				.andExpect(status().isOk());
	}


	@SpringBootApplication
	static class SpringDocTestApp {}

}