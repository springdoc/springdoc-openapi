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

package test.org.springdoc.api.v31.app285;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.StringNode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 *
 * @author bnasslahsen
 */
@RestController
public class HelloController {

	@GetMapping("/nodes")
	public JsonNodes nodes() {
		return null;
	}

	/**
	 * A model with a field of each Jackson 3 node type.
	 *
	 * @param any    any JSON value
	 * @param object a JSON object
	 * @param array  a JSON array
	 * @param string a JSON string
	 */
	public record JsonNodes(JsonNode any, ObjectNode object, ArrayNode array, StringNode string) {
	}

}
