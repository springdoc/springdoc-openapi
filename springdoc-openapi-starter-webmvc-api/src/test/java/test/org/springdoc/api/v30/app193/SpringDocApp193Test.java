/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app193;


import org.apache.commons.lang3.JavaVersion;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.api.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.apache.commons.lang3.SystemUtils.isJavaVersionAtLeast;
import static org.hamcrest.Matchers.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SpringDocApp193Test extends AbstractCommonTest {

	@Test
	public void testApp() throws Exception {
		final MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
		final String result = mockMvcResult.getResponse().getContentAsString();
		// In Java 21 the getFirst() and getLast() methods were added to the List interface.
		// Those are the POJO getters, therefore Jackson will add them during serialization.
		// So there are two different expected results for Java prior 21 and starting from Java 21.
		final var expectedResponseFile = isJavaVersionAtLeast(JavaVersion.JAVA_21) ? "app193-1.json" : "app193.json";
		final String expected = getContent("results/3.0.1/" + expectedResponseFile);
		assertEquals(expected, result, true);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
