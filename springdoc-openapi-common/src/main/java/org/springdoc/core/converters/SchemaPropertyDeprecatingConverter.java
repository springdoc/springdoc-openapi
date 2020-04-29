package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.stream.Stream;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;

import static org.springdoc.api.AbstractOpenApiResource.DEPRECATED_ANNOTATIONS;

public class SchemaPropertyDeprecatingConverter implements ModelConverter {

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (chain.hasNext()) {
			Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
			if (type.isSchemaProperty() && containsDeprecatedAnnotation(type.getCtxAnnotations()))
				resolvedSchema.setDeprecated(true);
			return resolvedSchema;
		}
		return null;
	}

	public static boolean containsDeprecatedAnnotation(Annotation[] annotations) {
		return annotations != null && Stream.of(annotations).map(Annotation::annotationType).anyMatch(DEPRECATED_ANNOTATIONS::contains);
	}
}
