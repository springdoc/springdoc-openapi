package org.springdoc.core.converters;

import java.util.Iterator;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.api.AbstractOpenApiResource;

public class SchemaPropertyDeprecatingConverter implements ModelConverter {
	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (chain.hasNext()) {
			Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
			if (type.isSchemaProperty() && AbstractOpenApiResource.containsDeprecatedAnnotation(type.getCtxAnnotations())) {
				resolvedSchema.setDeprecated(true);
			}
			return resolvedSchema;
		}
		return null;
	}
}
