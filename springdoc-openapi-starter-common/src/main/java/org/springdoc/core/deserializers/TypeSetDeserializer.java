/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.deserializers;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Reads an OpenAPI 3.1 schema {@code type} that swagger-core serializes as the scalar
 * {@code "type": "integer"} back into the {@code Set<String>} field, while still supporting
 * the array form {@code "type": ["string","null"]}.
 * <p>
 * Reported upstream with <a href="https://github.com/swagger-api/swagger-core/issues/5264">swagger-core issue 5264</a>
 *
 * @author Mattias-Sehlstedt
 */
public class TypeSetDeserializer extends JsonDeserializer<Set<String>> {

	@Override
	public Set<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode node = ctxt.readTree(p);
		if (node == null || node.isNull())
			return null;
		Set<String> types = new LinkedHashSet<>();
		if (node.isArray())
			node.forEach(typeNode -> {
				if (!typeNode.isNull())
					types.add(typeNode.asText());
			});
		else
			types.add(node.asText());
		return types.isEmpty() ? null : types;
	}

}
