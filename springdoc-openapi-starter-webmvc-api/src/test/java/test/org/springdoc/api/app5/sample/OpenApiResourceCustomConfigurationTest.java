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

package test.org.springdoc.api.app5.sample;

import org.junit.jupiter.api.Test;
import test.org.springdoc.api.AbstractSpringDocTest;
import test.org.springdoc.api.app5.CustomOpenAPIConfig;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CustomOpenAPIConfig.class)
@TestPropertySource(properties = "springdoc.api-docs.path=/api-docs")
public class OpenApiResourceCustomConfigurationTest extends AbstractSpringDocTest {

	/**
	 * givenNoConfiguration_whenGetApiJson_returnsDefaultEmptyDocs -  should return
	 * {"openapi":"3.0.1","info":{"title":"Custom API","version":"100"},"paths":{},"components":{}}
	 */
	@Test
	public void testApp() throws Exception {
		mockMvc
				.perform(get("/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(jsonPath("$.info.title", is("Custom API")))
				.andExpect(jsonPath("$.info.version", is("100")))
				.andExpect(jsonPath("$.paths").isEmpty())
				.andExpect(jsonPath("$.components").isEmpty())
				.andExpect(jsonPath("$.tags").isNotEmpty())
				.andExpect(jsonPath("$.tags[0].name", is("mytag")));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
