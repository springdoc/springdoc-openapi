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

package test.org.springdoc.ui.app1;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"springdoc.swagger-ui.path=/test/swagger.html",
		"server.servlet.context-path=/context-path",
		"spring.mvc.servlet.path=/servlet-path"
})
public class SpringDocConfigPathsTest extends AbstractSpringDocTest {

	@Test
	public void should_display_swaggerui_page() throws Exception {
		mockMvc.perform(get("/context-path/servlet-path/test/swagger.html").contextPath("/context-path").servletPath("/servlet-path")).andExpect(status().isFound()).andReturn();
		MvcResult mvcResult = mockMvc.perform(get("/context-path/servlet-path/test/swagger-ui/index.html").contextPath("/context-path").servletPath("/servlet-path")).andExpect(status().isOk()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		assertTrue(contentAsString.contains("Swagger UI"));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}