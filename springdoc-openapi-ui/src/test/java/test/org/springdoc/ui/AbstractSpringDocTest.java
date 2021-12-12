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

package test.org.springdoc.ui;

import org.springdoc.core.Constants;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

	public static String className;

	private static final String DEFAULT_SWAGGER_UI_URL=  Constants.SWAGGER_UI_URL;

	protected void checkHTML(String fileName, String uri)throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();
		String transformedIndex = mvcResult.getResponse().getContentAsString();
		assertTrue(transformedIndex.contains("Swagger UI"));
		assertEquals(this.getContent(fileName), transformedIndex);
	}
	protected void chekHTML(String fileName) throws Exception {
		checkHTML( fileName, DEFAULT_SWAGGER_UI_URL);
	}

	protected void chekHTML() throws Exception {
		className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		checkHTML( "results/app" + testNumber, DEFAULT_SWAGGER_UI_URL);
	}
}
