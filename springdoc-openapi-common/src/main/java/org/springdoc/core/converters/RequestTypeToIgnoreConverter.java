package org.springdoc.core.converters;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.AbstractRequestBuilder;

public class RequestTypeToIgnoreConverter implements ModelConverter {

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (type.isSchemaProperty()) {
			JavaType javaType = Json.mapper().constructType(type.getType());
			Class<?> cls = javaType.getRawClass();
			if (AbstractRequestBuilder.isRequestTypeToIgnore(cls))
				return null;
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}
}