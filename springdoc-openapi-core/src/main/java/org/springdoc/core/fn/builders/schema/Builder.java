/*
 *
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
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

package org.springdoc.core.fn.builders.schema;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Schema builder.
 * @author bnasslahsen
 */
public class Builder {
	/**
	 * Provides a java class as implementation for this schema.  When provided, additional information in the Schema annotation (except for type information) will augment the java class after introspection.
	 *
	 */
	private Class<?> implementation = Void.class;

	/**
	 * Provides a java class to be used to disallow matching properties.
	 *
	 */
	private Class<?> not = Void.class;

	/**
	 * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If more than one match the derived schemas, a validation error will occur.
	 *
	 */
	private Class<?>[] oneOf = {};

	/**
	 * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If any match, the schema will be considered valid.
	 *
	 */
	private Class<?>[] anyOf = {};

	/**
	 * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If all match, the schema will be considered valid
	 *
	 */
	private Class<?>[] allOf = {};

	/**
	 * The name of the schema or property.
	 *
	 */
	private String name = "";

	/**
	 * A title to explain the purpose of the schema.
	 *
	 */
	private String title = "";

	/**
	 * Constrains a value such that when divided by the multipleOf, the remainder must be an integer.  Ignored if the value is 0.
	 *
	 */
	private double multipleOf = 0;

	/**
	 * Sets the maximum numeric value for a property.  Ignored if the value is an empty string.
	 *
	 */
	private String maximum = "";

	/**
	 * if true, makes the maximum value exclusive, or a less-than criteria.
	 *
	 */
	private boolean exclusiveMaximum;

	/**
	 * Sets the minimum numeric value for a property.  Ignored if the value is an empty string or not a number.
	 *
	 */
	private String minimum = "";

	/**
	 * If true, makes the minimum value exclusive, or a greater-than criteria.
	 *
	 */
	private boolean exclusiveMinimum;

	/**
	 * Sets the maximum length of a string value.  Ignored if the value is negative.
	 *
	 */
	private int maxLength = Integer.MAX_VALUE;

	/**
	 * Sets the minimum length of a string value.  Ignored if the value is negative.
	 *
	 */
	private int minLength = 0;

	/**
	 * A pattern that the value must satisfy. Ignored if the value is an empty string.
	 *
	 */
	private String pattern = "";

	/**
	 * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
	 *
	 */
	private int maxProperties = 0;

	/**
	 * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
	 *
	 */
	private int minProperties = 0;

	/**
	 * Allows multiple properties in an object to be marked as required.
	 *
	 */
	private String[] requiredProperties = {};

	/**
	 * Mandates that the annotated item is required or not.
	 *
	 */
	private boolean required;

	/**
	 * A description of the schema.
	 *
	 */
	private String description = "";

	/**
	 * Provides an optional override for the format.  If a consumer is unaware of the meaning of the format, they shall fall back to using the basic type without format.  For example, if \&quot;type: integer, format: int128\&quot; were used to designate a very large integer, most consumers will not understand how to handle it, and fall back to simply \&quot;type: integer\&quot;
	 *
	 */
	private String format = "";

	/**
	 * References a schema definition in an external OpenAPI document.
	 *
	 */
	private String ref = "";

	/**
	 * If true, designates a value as possibly null.
	 *
	 */
	private boolean nullable;

	/**
	 * Allows to specify the access mode (AccessMode.READ_ONLY, READ_WRITE)
	 *
	 * AccessMode.READ_ONLY: value will not be written to during a request but may be returned during a response.
	 * AccessMode.WRITE_ONLY: value will only be written to during a request but not returned during a response.
	 * AccessMode.READ_WRITE: value will be written to during a request and returned during a response.
	 *
	 *
	 */
	private AccessMode accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO;

	/**
	 * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
	 *
	 */
	private String example = "";

	/**
	 * Additional external documentation for this schema.
	 *
	 */
	private ExternalDocumentation externalDocs = org.springdoc.core.fn.builders.externaldocumentation.Builder.externalDocumentationBuilder().build();

	/**
	 * Specifies that a schema is deprecated and should be transitioned out of usage.
	 *
	 */
	private boolean deprecated;

	/**
	 * Provides an override for the basic type of the schema.  Must be a valid type per the OpenAPI Specification.
	 *
	 */
	private String type = "";

	/**
	 * Provides a list of allowable values.  This field map to the enum property in the OAS schema.
	 *
	 */
	private String[] allowableValues = {};

	/**
	 * Provides a default value.
	 *
	 */
	private String defaultValue = "";

	/**
	 * Provides a discriminator property value.
	 *
	 */
	private String discriminatorProperty = "";

	/**
	 * Provides discriminator mapping values.
	 *
	 */
	private DiscriminatorMapping[] discriminatorMapping = {};

	/**
	 * Allows schema to be marked as hidden.
	 *
	 */
	private boolean hidden;

	/**
	 * Allows enums to be resolved as a reference to a scheme added to components section.
	 *
	 * @since swagger -core 2.1.0
	 * @return whether or not this must be resolved as a reference
	 */
	private boolean enumAsRef;

	/**
	 * An array of the sub types inheriting from this model.
	 */
	private Class<?>[] subTypes = {};

	/**
	 * The list of optional extensions
	 *
	 * @return an optional array of extensions
	 */
	private Extension[] extensions = {};


	/**
	 * Instantiates a new Schema builder.
	 */
	private Builder() {
	}

	/**
	 * Builder schema builder.
	 *
	 * @return the schema builder
	 */
	public static Builder schemaBuilder() {
		return new Builder();
	}

	/**
	 * Implementation schema builder.
	 *
	 * @param implementation the implementation
	 * @return the schema builder
	 */
	public Builder implementation(Class<?> implementation) {
		this.implementation = implementation;
		return this;
	}

	/**
	 * Not schema builder.
	 *
	 * @param not the not
	 * @return the schema builder
	 */
	public Builder not(Class<?> not) {
		this.not = not;
		return this;
	}

	/**
	 * One of schema builder.
	 *
	 * @param oneOf the one of
	 * @return the schema builder
	 */
	public Builder oneOf(Class<?>[] oneOf) {
		this.oneOf = oneOf;
		return this;
	}

	/**
	 * Any of schema builder.
	 *
	 * @param anyOf the any of
	 * @return the schema builder
	 */
	public Builder anyOf(Class<?>[] anyOf) {
		this.anyOf = anyOf;
		return this;
	}

	/**
	 * All of schema builder.
	 *
	 * @param allOf the all of
	 * @return the schema builder
	 */
	public Builder allOf(Class<?>[] allOf) {
		this.allOf = allOf;
		return this;
	}

	/**
	 * Name schema builder.
	 *
	 * @param name the name
	 * @return the schema builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Title schema builder.
	 *
	 * @param title the title
	 * @return the schema builder
	 */
	public Builder title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Multiple of schema builder.
	 *
	 * @param multipleOf the multiple of
	 * @return the schema builder
	 */
	public Builder multipleOf(double multipleOf) {
		this.multipleOf = multipleOf;
		return this;
	}

	/**
	 * Maximum schema builder.
	 *
	 * @param maximum the maximum
	 * @return the schema builder
	 */
	public Builder maximum(String maximum) {
		this.maximum = maximum;
		return this;
	}

	/**
	 * Exclusive maximum schema builder.
	 *
	 * @param exclusiveMaximum the exclusive maximum
	 * @return the schema builder
	 */
	public Builder exclusiveMaximum(boolean exclusiveMaximum) {
		this.exclusiveMaximum = exclusiveMaximum;
		return this;
	}

	/**
	 * Minimum schema builder.
	 *
	 * @param minimum the minimum
	 * @return the schema builder
	 */
	public Builder minimum(String minimum) {
		this.minimum = minimum;
		return this;
	}

	/**
	 * Exclusive minimum schema builder.
	 *
	 * @param exclusiveMinimum the exclusive minimum
	 * @return the schema builder
	 */
	public Builder exclusiveMinimum(boolean exclusiveMinimum) {
		this.exclusiveMinimum = exclusiveMinimum;
		return this;
	}

	/**
	 * Max length schema builder.
	 *
	 * @param maxLength the max length
	 * @return the schema builder
	 */
	public Builder maxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	/**
	 * Min length schema builder.
	 *
	 * @param minLength the min length
	 * @return the schema builder
	 */
	public Builder minLength(int minLength) {
		this.minLength = minLength;
		return this;
	}

	/**
	 * Pattern schema builder.
	 *
	 * @param pattern the pattern
	 * @return the schema builder
	 */
	public Builder pattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	/**
	 * Max properties schema builder.
	 *
	 * @param maxProperties the max properties
	 * @return the schema builder
	 */
	public Builder maxProperties(int maxProperties) {
		this.maxProperties = maxProperties;
		return this;
	}

	/**
	 * Min properties schema builder.
	 *
	 * @param minProperties the min properties
	 * @return the schema builder
	 */
	public Builder minProperties(int minProperties) {
		this.minProperties = minProperties;
		return this;
	}

	/**
	 * Required properties schema builder.
	 *
	 * @param requiredProperties the required properties
	 * @return the schema builder
	 */
	public Builder requiredProperties(String[] requiredProperties) {
		this.requiredProperties = requiredProperties;
		return this;
	}

	/**
	 * Required schema builder.
	 *
	 * @param required the required
	 * @return the schema builder
	 */
	public Builder required(boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * Description schema builder.
	 *
	 * @param description the description
	 * @return the schema builder
	 */
	public Builder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Format schema builder.
	 *
	 * @param format the format
	 * @return the schema builder
	 */
	public Builder format(String format) {
		this.format = format;
		return this;
	}

	/**
	 * Ref schema builder.
	 *
	 * @param ref the ref
	 * @return the schema builder
	 */
	public Builder ref(String ref) {
		this.ref = ref;
		return this;
	}

	/**
	 * Nullable schema builder.
	 *
	 * @param nullable the nullable
	 * @return the schema builder
	 */
	public Builder nullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	/**
	 * Access mode schema builder.
	 *
	 * @param accessMode the access mode
	 * @return the schema builder
	 */
	public Builder accessMode(AccessMode accessMode) {
		this.accessMode = accessMode;
		return this;
	}

	/**
	 * Example schema builder.
	 *
	 * @param example the example
	 * @return the schema builder
	 */
	public Builder example(String example) {
		this.example = example;
		return this;
	}

	/**
	 * External docs schema builder.
	 *
	 * @param externalDocumentationBuilder the external documentation builder
	 * @return the schema builder
	 */
	public Builder externalDocs(org.springdoc.core.fn.builders.externaldocumentation.Builder externalDocumentationBuilder) {
		this.externalDocs = externalDocumentationBuilder.build();
		return this;
	}

	/**
	 * Deprecated schema builder.
	 *
	 * @param deprecated the deprecated
	 * @return the schema builder
	 */
	public Builder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	/**
	 * Type schema builder.
	 *
	 * @param type the type
	 * @return the schema builder
	 */
	public Builder type(String type) {
		this.type = type;
		return this;
	}

	/**
	 * Allowable values schema builder.
	 *
	 * @param allowableValues the allowable values
	 * @return the schema builder
	 */
	public Builder allowableValues(String[] allowableValues) {
		this.allowableValues = allowableValues;
		return this;
	}

	/**
	 * Default value schema builder.
	 *
	 * @param defaultValue the default value
	 * @return the schema builder
	 */
	public Builder defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	/**
	 * Discriminator property schema builder.
	 *
	 * @param discriminatorProperty the discriminator property
	 * @return the schema builder
	 */
	public Builder discriminatorProperty(String discriminatorProperty) {
		this.discriminatorProperty = discriminatorProperty;
		return this;
	}

	/**
	 * Discriminator mapping schema builder.
	 *
	 * @param discriminatorMappingBuilder the discriminator mapping builder
	 * @return the schema builder
	 */
	public Builder discriminatorMapping(org.springdoc.core.fn.builders.discriminatormapping.Builder discriminatorMappingBuilder) {
		this.discriminatorMapping = ArrayUtils.add(this.discriminatorMapping, discriminatorMappingBuilder.build());
		return this;
	}

	/**
	 * Hidden schema builder.
	 *
	 * @param hidden the hidden
	 * @return the schema builder
	 */
	public Builder hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	/**
	 * Enum as ref schema builder.
	 *
	 * @param enumAsRef the enum as ref
	 * @return the schema builder
	 */
	public Builder enumAsRef(boolean enumAsRef) {
		this.enumAsRef = enumAsRef;
		return this;
	}

	/**
	 * Sub types schema builder.
	 *
	 * @param subTypes the sub types
	 * @return the schema builder
	 */
	public Builder subTypes(Class<?>[] subTypes) {
		this.subTypes = subTypes;
		return this;
	}

	/**
	 * Extensions schema builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the schema builder
	 */
	public Builder extensions(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build schema.
	 *
	 * @return the schema
	 */
	public Schema build() {
		return new Schema() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public Class<?> implementation() {
				return implementation;
			}

			@Override
			public Class<?> not() {
				return not;
			}

			@Override
			public Class<?>[] oneOf() {
				return oneOf;
			}

			@Override
			public Class<?>[] anyOf() {
				return anyOf;
			}

			@Override
			public Class<?>[] allOf() {
				return allOf;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String title() {
				return title;
			}

			@Override
			public double multipleOf() {
				return multipleOf;
			}

			@Override
			public String maximum() {
				return maximum;
			}

			@Override
			public boolean exclusiveMaximum() {
				return exclusiveMaximum;
			}

			@Override
			public String minimum() {
				return minimum;
			}

			@Override
			public boolean exclusiveMinimum() {
				return exclusiveMinimum;
			}

			@Override
			public int maxLength() {
				return maxLength;
			}

			@Override
			public int minLength() {
				return minLength;
			}

			@Override
			public String pattern() {
				return pattern;
			}

			@Override
			public int maxProperties() {
				return maxProperties;
			}

			@Override
			public int minProperties() {
				return minProperties;
			}

			@Override
			public String[] requiredProperties() {
				return requiredProperties;
			}

			@Override
			public boolean required() {
				return required;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public String format() {
				return format;
			}

			@Override
			public String ref() {
				return ref;
			}

			@Override
			public boolean nullable() {
				return nullable;
			}

			@Override
			public boolean readOnly() {
				return AccessMode.READ_ONLY.equals(accessMode);
			}

			@Override
			public boolean writeOnly() {
				return AccessMode.WRITE_ONLY.equals(accessMode);
			}

			@Override
			public AccessMode accessMode() {
				return accessMode;
			}

			@Override
			public String example() {
				return example;
			}

			@Override
			public ExternalDocumentation externalDocs() {
				return externalDocs;
			}

			@Override
			public boolean deprecated() {
				return deprecated;
			}

			@Override
			public String type() {
				return type;
			}

			@Override
			public String[] allowableValues() {
				return allowableValues;
			}

			@Override
			public String defaultValue() {
				return defaultValue;
			}

			@Override
			public String discriminatorProperty() {
				return discriminatorProperty;
			}

			@Override
			public DiscriminatorMapping[] discriminatorMapping() {
				return discriminatorMapping;
			}

			@Override
			public boolean hidden() {
				return hidden;
			}

			@Override
			public boolean enumAsRef() {
				return enumAsRef;
			}

			@Override
			public Class<?>[] subTypes() {
				return subTypes;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
	}
}
