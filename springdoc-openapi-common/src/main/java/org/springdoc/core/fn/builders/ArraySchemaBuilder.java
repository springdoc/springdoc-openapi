package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;

public class ArraySchemaBuilder {
	/**
	 * The schema of the items in the array
	 *
	 */
	private Schema schema = SchemaBuilder.builder().build();

	/**
	 * Allows to define the properties to be resolved into properties of the schema of type `array` (not the ones of the
	 * `items` of such schema which are defined in schema}.
	 *
	 */
	private Schema arraySchema = SchemaBuilder.builder().build();

	/**
	 * sets the maximum number of items in an array.  Ignored if value is Integer.MIN_VALUE.
	 *
	 **/
	private int maxItems = Integer.MIN_VALUE;

	/**
	 * sets the minimum number of items in an array.  Ignored if value is Integer.MAX_VALUE.
	 *
	 **/
	private int minItems = Integer.MAX_VALUE;

	/**
	 * determines whether an array of items will be unique
	 *
	 **/
	private boolean uniqueItems;

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};


	private ArraySchemaBuilder() {
	}

	public static ArraySchemaBuilder builder() {
		return new ArraySchemaBuilder();
	}

	public ArraySchemaBuilder schema(SchemaBuilder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	public ArraySchemaBuilder arraySchema(SchemaBuilder schemaBuilder) {
		this.arraySchema = schemaBuilder.build();
		return this;
	}

	public ArraySchemaBuilder maxItems(int maxItems) {
		this.maxItems = maxItems;
		return this;
	}

	public ArraySchemaBuilder minItems(int minItems) {
		this.minItems = minItems;
		return this;
	}

	public ArraySchemaBuilder uniqueItems(boolean uniqueItems) {
		this.uniqueItems = uniqueItems;
		return this;
	}

	public ArraySchemaBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

	public ArraySchema build() {
		ArraySchema arraySchemaResult = new ArraySchema() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public Schema schema() {
				return schema;
			}

			@Override
			public Schema arraySchema() {
				return arraySchema;
			}

			@Override
			public int maxItems() {
				return maxItems;
			}

			@Override
			public int minItems() {
				return minItems;
			}

			@Override
			public boolean uniqueItems() {
				return uniqueItems;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};

		return arraySchemaResult;
	}
}
