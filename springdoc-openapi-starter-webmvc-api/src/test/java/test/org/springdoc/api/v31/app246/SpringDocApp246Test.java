/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v31.app246;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocV31Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * This test class verifies the webhook filtering functionality based on package scanning.
 * It ensures that only webhooks from the packages defined in {@code springdoc.packages-to-scan}
 * are included in the OpenAPI specification, and webhooks from packages in
 * {@code springdoc.packages-to-exclude} are correctly omitted.
 */
@SpringBootTest(classes = SpringDocApp246Test.SpringDocApp246.class)
@TestPropertySource(properties = {
		"springdoc.packages-to-scan=test.org.springdoc.api.v31.app246",
		"springdoc.packages-to-exclude=test.org.springdoc.api.v31.app246.excluded",
		"springdoc.api-docs.version=OPENAPI_3_1"
})
public class SpringDocApp246Test extends AbstractSpringDocV31Test {

	@SpringBootApplication
	static class SpringDocApp246 {
	}

	@Test
	public void testApp2() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.webhooks.includedPet.post.requestBody.description", is("Information about a new pet in the system")))
				.andExpect(jsonPath("$.webhooks.includedNewPet.post.requestBody.description", is("Information about a new pet in the system")))
				.andExpect(jsonPath("$.webhooks.excludedNewPet").doesNotExist());
	}
}
