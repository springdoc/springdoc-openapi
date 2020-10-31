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

package test.org.springdoc.api.app139;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test issue 907 fix.
 * Hidden controller showing up in swagger UI when springdoc.show-actuator is enabled
 */
@TestPropertySource(properties = "springdoc.show-actuator=true")
public class SpringDocApp139Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

	@Test
	public void testApp() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(jsonPath("$.paths./actuator/info.get.operationId", containsString("handle_")))
				.andExpect(jsonPath("$.paths./actuator/info.get.summary", Matchers.is("Actuator web endpoint 'info'")))
				.andExpect(jsonPath("$.paths./actuator/health.get.operationId", containsString("handle_")))
				.andReturn();
		String result = mockMvcResult.getResponse().getContentAsString();
		String expected = getContent("results/app139.json");
		Assertions.assertEquals(expected, result);
	}
}
