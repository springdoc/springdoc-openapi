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

import java.util.LinkedHashSet;
import java.util.Set;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

/**
 * Reads an OpenAPI 3.1 schema {@code type} that swagger-core serializes as the scalar
 * {@code "type": "integer"} back into the {@code Set<String>} field, while still supporting
 * the array form {@code "type": ["string","null"]}.
 * <p>
 * Reported upstream with <a href="https://github.com/swagger-api/swagger-core/issues/5264">swagger-core issue 5264</a>
 *
 * @author Mattias-Sehlstedt
 */
public class TypeSetDeserializer extends ValueDeserializer<Set<String>> {

	@Override
	public Set<String> deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
		JsonNode node = ctxt.readTree(p);
		if (node == null || node.isNull())
			return null;
		Set<String> types = new LinkedHashSet<>();
		if (node.isArray())
			node.forEach(typeNode -> {
				if (!typeNode.isNull())
					types.add(typeNode.asString());
			});
		else
			types.add(node.asString());
		return types.isEmpty() ? null : types;
	}

}
