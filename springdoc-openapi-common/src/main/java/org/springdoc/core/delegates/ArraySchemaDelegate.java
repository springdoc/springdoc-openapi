package org.springdoc.core.delegates;


import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Utility class to delegate to another @ArraySchema annotation.
 * @author daniel-shuy
 */
public class ArraySchemaDelegate implements ArraySchema {
	private final ArraySchema arraySchema;

	/**
	 * Create a delegate to the given ArraySchema.
	 * @param arraySchema The ArraySchema annotation to delegate to.
	 */
	public ArraySchemaDelegate(ArraySchema arraySchema) {
		this.arraySchema = arraySchema;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return arraySchema.annotationType();
	}

	@Override
	public Schema schema() {
		return arraySchema.schema();
	}

	@Override
	public Schema arraySchema() {
		return arraySchema.arraySchema();
	}

	@Override
	public int maxItems() {
		return arraySchema.maxItems();
	}

	@Override
	public int minItems() {
		return arraySchema.minItems();
	}

	@Override
	public boolean uniqueItems() {
		return arraySchema.uniqueItems();
	}

	@Override
	public Extension[] extensions() {
		return arraySchema.extensions();
	}
}