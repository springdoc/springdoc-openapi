/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v31.app44;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Spring doc app 44 test.
 */
class SpringDocApp44Test extends AbstractSpringDocTest {

	/**
	 * Test app.
	 *
	 * @throws Exception the exception
	 */
	@Test
	protected void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0"))).andExpect(jsonPath("$.paths./helloworld.post.responses.200.content.['application/json'].schema.oneOf").isArray()).andExpect(jsonPath("$.paths./helloworld.post.responses.200.content.['application/json'].schema.oneOf[*].$ref", containsInAnyOrder("#/components/schemas/HelloDTO2",
						"#/components/schemas/HelloDTO1")))
				.andExpect(jsonPath("$.paths./helloworld.post.requestBody.content.['application/vnd.v1+json'].schema.$ref", is("#/components/schemas/RequestV1")))
				.andExpect(jsonPath("$.paths./helloworld.post.requestBody.content.['application/vnd.v2+json'].schema.$ref", is("#/components/schemas/RequestV2")))
				.andExpect(jsonPath("$.paths./helloworld.post.responses.400.content.['application/json'].schema.$ref", is("#/components/schemas/ErrorDTO")));

	}

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {}

}