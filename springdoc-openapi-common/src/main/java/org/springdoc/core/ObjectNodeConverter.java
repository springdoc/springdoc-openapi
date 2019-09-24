package org.springdoc.core;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;

public class ObjectNodeConverter implements ModelConverter {

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType _type = Json.mapper().constructType(type.getType());
		if (_type != null) {
			Class<?> cls = _type.getRawClass();
			if (ObjectNode.class.isAssignableFrom(cls)) {
				return new ObjectSchema();
			}
		}
		if (chain.hasNext()) {
			return chain.next().resolve(type, context, chain);
		} else {
			return null;
		}
	}
}