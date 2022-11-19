/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core.fn.builders.arrayschema;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Array schema builder.
 * @author bnasslahsen
 */
public class Builder {
	/**
	 * The schema of the items in the array
	 *
	 */
	private Schema schema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * Allows to define the properties to be resolved into properties of the schema of type `array` (not the ones of the
	 * `items` of such schema which are defined in schema}.
	 *
	 */
	private Schema arraySchema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * sets the maximum number of items in an array.  Ignored if value is Integer.MIN_VALUE.
	 *
	 */
	private int maxItems = Integer.MIN_VALUE;

	/**
	 * sets the minimum number of items in an array.  Ignored if value is Integer.MAX_VALUE.
	 *
	 */
	private int minItems = Integer.MAX_VALUE;

	/**
	 * determines whether an array of items will be unique
	 *
	 */
	private boolean uniqueItems;

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};


	/**
	 * Instantiates a new Array schema builder.
	 */
	private Builder() {
	}

	/**
	 * Builder array schema builder.
	 *
	 * @return the array schema builder
	 */
	public static Builder arraySchemaBuilder() {
		return new Builder();
	}

	/**
	 * Schema array schema builder.
	 *
	 * @param schemaBuilder the schema builder
	 * @return the array schema builder
	 */
	public Builder schema(org.springdoc.core.fn.builders.schema.Builder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	/**
	 * Array schema array schema builder.
	 *
	 * @param schemaBuilder the schema builder
	 * @return the array schema builder
	 */
	public Builder arraySchema(org.springdoc.core.fn.builders.schema.Builder schemaBuilder) {
		this.arraySchema = schemaBuilder.build();
		return this;
	}

	/**
	 * Max items array schema builder.
	 *
	 * @param maxItems the max items
	 * @return the array schema builder
	 */
	public Builder maxItems(int maxItems) {
		this.maxItems = maxItems;
		return this;
	}

	/**
	 * Min items array schema builder.
	 *
	 * @param minItems the min items
	 * @return the array schema builder
	 */
	public Builder minItems(int minItems) {
		this.minItems = minItems;
		return this;
	}

	/**
	 * Unique items array schema builder.
	 *
	 * @param uniqueItems the unique items
	 * @return the array schema builder
	 */
	public Builder uniqueItems(boolean uniqueItems) {
		this.uniqueItems = uniqueItems;
		return this;
	}

	/**
	 * Extension array schema builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the array schema builder
	 */
	public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build array schema.
	 *
	 * @return the array schema
	 */
	public ArraySchema build() {
		return new ArraySchema() {
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
	}
}
