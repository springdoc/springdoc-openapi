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

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.utils.Constants;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

	public static String className;

	protected void checkJS(String fileName, String uri, String contextPath) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(contextPath + uri).contextPath(contextPath)).andExpect(status().isOk()).andReturn();
		String transformedIndex = mvcResult.getResponse().getContentAsString();
		assertTrue(transformedIndex.contains("window.ui"));
		assertEquals("no-store", mvcResult.getResponse().getHeader("Cache-Control"));
		assertEquals(this.getContent(fileName), transformedIndex.replace("\r", ""));
	}

	protected void chekJS(String fileName, String contextPath) throws Exception {
		checkJS(fileName, Constants.SWAGGER_INITIALIZER_URL, contextPath);
	}

	protected void chekJS(String contextPath) throws Exception {
		className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		checkJS("results/app" + testNumber, Constants.SWAGGER_INITIALIZER_URL, contextPath);
	}

	protected void chekJS() throws Exception {
		className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		checkJS("results/app" + testNumber, Constants.SWAGGER_INITIALIZER_URL, StringUtils.EMPTY);
	}

	protected void checkJSResult(String fileName, String htmlResult) {
		assertTrue(htmlResult.contains("window.ui"));
		assertEquals(this.getContent(fileName), htmlResult.replace("\r", ""));
	}
}
