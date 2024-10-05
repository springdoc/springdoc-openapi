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
package test.org.springdoc.api.v30.app134;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Tests Spring meta-annotations as method parameters
 */
public class SpringDocApp134Test extends AbstractSpringDocV30Test {

	@Test
	protected void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/v1-group"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(content().json(getContent("results/3.0.1/app134-1.json"), true));
	}

	@Test
	void testApp2() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/v2-group"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(content().json(getContent("results/3.0.1/app134-2.json"), true));
	}

	@Test
	void testApp3() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/v1-headers-group"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(content().json(getContent("results/3.0.1/app134-3.json"), true));
	}

	@Test
	void testApp4() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/v1-v2-headers-group"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(content().json(getContent("results/3.0.1/app134-4.json"), true));
	}

	@Test
	void testApp5() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/v2-consumes-group"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(content().json(getContent("results/3.0.1/app134-5.json"), true));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
