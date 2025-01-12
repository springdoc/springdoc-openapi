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

package test.org.springdoc.api.v31.app178;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpringDocApp178Test extends AbstractSpringDocTest {

	@Test
	void testFilterOnlyPicksUpMatchedMethods() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/annotatedGroup1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app178-1.json"), true));
	}

	@Test
	void testFilterOnlyPicksUpMatchedMethodsWithDifferentFilter() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/annotatedGroup2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app178-2.json"), true));
	}

	@Test
	void testFilterOnlyPicksUpCombinedMatchedMethods() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/annotatedCombinedGroup"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app178-3.json"), true));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}