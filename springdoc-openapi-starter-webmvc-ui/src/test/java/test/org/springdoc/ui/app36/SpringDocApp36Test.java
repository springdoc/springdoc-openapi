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
 *  *  *  *  *  *
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *
 */

package test.org.springdoc.ui.app36;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocApp36Test extends AbstractSpringDocTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testEnumDescription() throws Exception {
		MvcResult result = mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		JsonNode jsonNode = objectMapper.readTree(content);

		String description = jsonNode.at("/paths/~1test/post/description").asText("");

		assertTrue(description.contains("**status**"), "Should contain status enum description");
		assertTrue(description.contains("`ACTIVE`"), "Should contain ACTIVE enum value");
		assertTrue(description.contains("`INACTIVE`"), "Should contain INACTIVE enum value");
		assertTrue(description.contains("Active status"), "Should contain ACTIVE description");
		assertTrue(description.contains("Inactive status"), "Should contain INACTIVE description");
	}

	@Test
	void testEnumDescriptionWithCustomFieldName() throws Exception {
		MvcResult result = mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		JsonNode jsonNode = objectMapper.readTree(content);

		String description = jsonNode.at("/paths/~1test2/get/description").asText("");

		assertTrue(description.contains("**priority**"), "Should contain priority enum description");
		assertTrue(description.contains("`HIGH`"), "Should contain HIGH enum value");
		assertTrue(description.contains("`LOW`"), "Should contain LOW enum value");
		assertTrue(description.contains("High priority"), "Should contain HIGH label from custom field");
		assertTrue(description.contains("Low priority"), "Should contain LOW label from custom field");
	}

	@SpringBootApplication
	static class SpringDocTestApp {
	}
}