/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package org.springdoc.core.fn.builders.header;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Header builder.
 *
 * @author bnasslahsen
 */
public class Builder {

	/**
	 * Required: The name of the header. The name is only used as the key to store this header in a map.
	 */
	private String name;

	/**
	 * Additional description data to provide on the purpose of the header
	 */
	private String description = "";

	/**
	 * The schema defining the type used for the header. Ignored if the properties content or array are specified.
	 */
	private Schema schema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * Determines whether this header is mandatory. The property may be included and its default value is false.
	 */
	private boolean required;

	/**
	 * Specifies that a header is deprecated and should be transitioned out of usage.
	 */
	private boolean deprecated;

	/**
	 * A reference to a header defined in components headers.
	 *
	 * @since swagger -core 2.0.3
	 */
	private String ref = "";

	/**
	 * The Explode.
	 */
	private Explode explode = Explode.DEFAULT;

	/**
	 * The Hidden.
	 */
	private boolean hidden;

	/**
	 * The Example.
	 */
	private String example = "";

	/**
	 * The Examples.
	 */
	private ExampleObject[] examples = {};

	/**
	 * Array array schema.
	 */
	private ArraySchema array = org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder().build();

	/**
	 * Instantiates a new Header builder.
	 */
	private Builder() {
	}

	/**
	 * Builder header builder.
	 *
	 * @return the header builder
	 */
	public static Builder headerBuilder() {
		return new Builder();
	}

	/**
	 * Name header builder.
	 *
	 * @param name the name
	 * @return the header builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Description header builder.
	 *
	 * @param description the description
	 * @return the header builder
	 */
	public Builder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Schema header builder.
	 *
	 * @param schemaBuilder the schema builder
	 * @return the header builder
	 */
	public Builder schema(org.springdoc.core.fn.builders.schema.Builder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	/**
	 * Required header builder.
	 *
	 * @param required the required
	 * @return the header builder
	 */
	public Builder required(boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * Deprecated header builder.
	 *
	 * @param deprecated the deprecated
	 * @return the header builder
	 */
	public Builder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	/**
	 * Ref header builder.
	 *
	 * @param ref the ref
	 * @return the header builder
	 */
	public Builder ref(String ref) {
		this.ref = ref;
		return this;
	}

	/**
	 * Explode builder.
	 *
	 * @param val the val
	 * @return the builder
	 */
	public Builder explode(Explode val) {
		explode = val;
		return this;
	}

	/**
	 * Hidden builder.
	 *
	 * @param val the val
	 * @return the builder
	 */
	public Builder hidden(boolean val) {
		hidden = val;
		return this;
	}

	/**
	 * Example builder.
	 *
	 * @param val the val
	 * @return the builder
	 */
	public Builder example(String val) {
		example = val;
		return this;
	}

	/**
	 * Examples builder.
	 *
	 * @param val the val
	 * @return the builder
	 */
	public Builder examples(ExampleObject[] val) {
		examples = val;
		return this;
	}

	/**
	 * Build header.
	 *
	 * @return the header
	 */
	public Header build() {
		return new Header() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public Schema schema() {
				return schema;
			}

			@Override
			public boolean required() {
				return required;
			}

			@Override
			public boolean deprecated() {
				return deprecated;
			}

			@Override
			public String ref() {
				return ref;
			}

			@Override
			public Explode explode() {
				return explode;
			}

			@Override
			public boolean hidden() {
				return hidden;
			}

			@Override
			public String example() {
				return example;
			}

			@Override
			public ExampleObject[] examples() {
				return examples;
			}

			@Override
			public ArraySchema array() {
				return array;
			}
		};
	}
}

