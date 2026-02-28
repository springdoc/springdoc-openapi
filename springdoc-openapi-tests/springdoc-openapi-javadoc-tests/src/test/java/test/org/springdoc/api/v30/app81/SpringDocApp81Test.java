/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app81;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Spring doc app 81 test.
 */
class SpringDocApp81Test extends AbstractSpringDocTest {

	/**
	 * Test app.
	 *
	 * @throws Exception the exception
	 */
	@Test
	protected void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(jsonPath("$.paths./api.get.tags[0]", containsString("hello-controller")))
				.andExpect(jsonPath("$.paths./api.get.operationId", startsWith("test")))
				.andExpect(jsonPath("$.paths./api.get.responses.200.content.['*/*'].schema.type", is("string")))
				.andExpect(jsonPath("$.paths./api.post.tags[0]", containsString("hello-controller")))
				.andExpect(jsonPath("$.paths./api.post.operationId", startsWith("test")))
				.andExpect(jsonPath("$.paths./api.post.responses.200.content.['*/*'].schema.type", is("string")))
				.andExpect(jsonPath("$.paths./api.put.tags[0]", containsString("hello-controller")))
				.andExpect(jsonPath("$.paths./api.put.operationId", startsWith("test")))
				.andExpect(jsonPath("$.paths./api.put.responses.200.content.['*/*'].schema.type", is("string")))
				.andExpect(jsonPath("$.paths./api.patch.tags[0]", containsString("hello-controller")))
				.andExpect(jsonPath("$.paths./api.patch.operationId", startsWith("test")))
				.andExpect(jsonPath("$.paths./api.patch.responses.200.content.['*/*'].schema.type", is("string")))
				.andExpect(jsonPath("$.paths./api.delete.tags[0]", containsString("hello-controller")))
				.andExpect(jsonPath("$.paths./api.delete.operationId", startsWith("test")))
				.andExpect(jsonPath("$.paths./api.delete.responses.200.content.['*/*'].schema.type", is("string")))
				.andExpect(jsonPath("$.paths./api.options.tags[0]", containsString("hello-controller")))
				.andExpect(jsonPath("$.paths./api.options.operationId", startsWith("test")))
				.andExpect(jsonPath("$.paths./api.options.responses.200.content.['*/*'].schema.type", is("string")))
				.andExpect(jsonPath("$.paths./api.head.tags[0]", containsString("hello-controller")))
				.andExpect(jsonPath("$.paths./api.head.operationId", startsWith("test")))
				.andExpect(jsonPath("$.paths./api.head.responses.200.content.['*/*'].schema.type", is("string")));
	}

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {}
}
