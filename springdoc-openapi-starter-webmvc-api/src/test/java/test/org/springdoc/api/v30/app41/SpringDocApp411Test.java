/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app41;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocApp411Test extends AbstractSpringDocV30Test {

	@Test
	protected void testApp() throws Exception {
		String className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
		// Test result consistency
		mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
		String result = mockMvcResult.getResponse().getContentAsString();
		Path path = Paths.get(getClass().getClassLoader().getResource("results/3.0.1/app41.json").toURI());
		byte[] fileBytes = Files.readAllBytes(path);
		String expected = new String(fileBytes);
		assertEquals(expected, result, false);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}