package org.springdoc.core.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Reads an OpenAPI 3.1 schema "type" that swagger-core serializes as a scalar
 * {@code "type": "integer"} back into the Set<String> field, while still supporting
 * the array form {@code "type": ["string","null"]}.
 * <p>
 * Reported upstream with <a href="https://github.com/swagger-api/swagger-core/issues/5264">swagger-core issue 5264</a>
 */
public class TypeSetDeserializer extends JsonDeserializer<Set<String>> {

    @Override
    public Set<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = ctxt.readTree(p);
        if (node == null || node.isNull()) {
            return null;
        }
        Set<String> types = new LinkedHashSet<>();
        if (node.isArray()) {
            node.forEach(n -> {
                if (!n.isNull()) {
                    types.add(n.asText());
                }
            });
        }
        else {
            types.add(node.asText());
        }
        return types.isEmpty() ? null : types;
    }

}
