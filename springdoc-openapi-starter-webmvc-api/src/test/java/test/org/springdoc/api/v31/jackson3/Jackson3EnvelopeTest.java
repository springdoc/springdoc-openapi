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

package test.org.springdoc.api.v31.jackson3;

import java.util.List;

import org.junit.jupiter.api.Test;
import test.org.springdoc.api.AbstractCommonTest;
import tools.jackson.databind.node.ObjectNode;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(Jackson3EnvelopeTest.ItemsController.class)
class Jackson3EnvelopeTest extends AbstractCommonTest {

	@Test
	void documentsGenericResponseWithJackson3Metadata() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.paths['/items'].get.responses['200'].content['application/json'].schema['$ref']")
				.value("#/components/schemas/EnvelopeListItem"))
			.andExpect(jsonPath("$.components.schemas.EnvelopeListItem.properties.data.type").value("array"))
			.andExpect(jsonPath("$.components.schemas.EnvelopeListItem.properties.meta.type").value("object"));
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

	@RestController
	static class ItemsController {

		@GetMapping(value = "/items", produces = "application/json")
		public Envelope<List<Item>> items() {
			return new Envelope<>(List.of(new Item("example")), null);
		}
	}

	record Envelope<T>(T data, ObjectNode meta) {}

	record Item(String name) {}
}
