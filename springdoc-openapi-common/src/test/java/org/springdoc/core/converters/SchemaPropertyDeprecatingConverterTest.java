package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Frąk
 */
class SchemaPropertyDeprecatingConverterTest {

	private SchemaPropertyDeprecatingConverter schemaPropertyDeprecatingConverter;

	@BeforeEach
	void setUp() {
		schemaPropertyDeprecatingConverter = new SchemaPropertyDeprecatingConverter();
	}

	@Test
	void shouldResolvePrimitives() {
		AnnotatedType type = mockAnnotatedTypeWithDeprecatedAnnotation();
		Schema<?> resolvedSchema = createPrimitiveSchema();

		ModelConverterContext context = mock(ModelConverterContext.class);
		ModelConverter modelConverter = mock(ModelConverter.class);
		Iterator<ModelConverter> chain = singletonList(modelConverter).iterator();
		when(modelConverter.resolve(type, context, chain))
				.thenReturn(resolvedSchema);

		Schema<?> result = schemaPropertyDeprecatingConverter.resolve(type, context, chain);

		assertNull(result.getAllOf());
		assertEquals(resolvedSchema, result);
		assertTrue(result.getDeprecated());
	}

	private AnnotatedType mockAnnotatedTypeWithDeprecatedAnnotation() {
		Deprecated deprecatedAnnotation = AnnotationHolder.class.getAnnotation(Deprecated.class);

		AnnotatedType type = new AnnotatedType();
		type.schemaProperty(true);
		type.ctxAnnotations(new Annotation[] { deprecatedAnnotation });
		return type;
	}

	private Schema<?> createPrimitiveSchema() {
		Schema<?> resolvedSchema = new Schema<>();
		resolvedSchema.setType("string");
		return resolvedSchema;
	}

	@Test
	void shouldResolveRefs() {
		AnnotatedType type = mockAnnotatedTypeWithDeprecatedAnnotation();
		Schema<?> resolvedSchema = createResolvedSchemaWithRef();

		ModelConverterContext context = mock(ModelConverterContext.class);
		ModelConverter modelConverter = mock(ModelConverter.class);
		Iterator<ModelConverter> chain = singletonList(modelConverter).iterator();
		when(modelConverter.resolve(type, context, chain))
				.thenReturn(resolvedSchema);

		Schema<?> result = schemaPropertyDeprecatingConverter.resolve(type, context, chain);

		assertNotNull(result.getAllOf());
		assertEquals(singletonList(resolvedSchema), result.getAllOf());
		assertTrue(result.getDeprecated());
	}

	private Schema<?> createResolvedSchemaWithRef() {
		Schema<?> resolvedSchema = new Schema<>();
		resolvedSchema.set$ref("#/components/schemas/TestClass");
		return resolvedSchema;
	}

	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated
	private static class AnnotationHolder {
	}
}