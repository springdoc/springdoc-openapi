/*
 *
 *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.ui.app36;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"springdoc.swagger-ui.disable-swagger-default-url=true",
		"springdoc.swagger-ui.path=/documentation/swagger-ui.html"
})
public class SpringDocApp36Test extends AbstractSpringDocTest {

	@Test
	void testWebJarResourceTransformed() throws Exception {
		mockMvc.perform(get("/webjars/swagger-ui/swagger-initializer.js"))
				.andExpect(status().isOk())
				.andExpect(content().string(not(containsString("https://petstore.swagger.io/v2/swagger.json"))))
				.andExpect(content().string(containsString("/v3/api-docs")));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
