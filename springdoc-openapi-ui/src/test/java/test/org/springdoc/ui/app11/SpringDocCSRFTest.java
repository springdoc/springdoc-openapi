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

package test.org.springdoc.ui.app11;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"springdoc.swagger-ui.csrf.enabled=true",
		"springdoc.swagger-ui.csrf.cookie-name=XSRF-TOKEN",
		"springdoc.swagger-ui.csrf.header-name=X-XSRF-TOKEN"
})
public class SpringDocCSRFTest extends AbstractSpringDocTest {

	@Test
	public void testApp() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/swagger-ui.html"))
				.andExpect(status().isFound()).andReturn();
		Cookie cookie = mvcResult.getResponse().getCookie("XSRF-TOKEN");
		mockMvc.perform(post("/post").header("X-XSRF-TOKEN", cookie.getValue()).cookie(cookie))
				.andExpect(status().isOk());
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}