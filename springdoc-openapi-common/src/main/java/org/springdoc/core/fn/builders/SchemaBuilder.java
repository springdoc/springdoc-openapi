package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import org.apache.commons.lang3.ArrayUtils;

public class SchemaBuilder {
	/**
	 * Provides a java class as implementation for this schema.  When provided, additional information in the Schema annotation (except for type information) will augment the java class after introspection.
	 *
	 **/
	private Class<?> implementation = Void.class;

	/**
	 * Provides a java class to be used to disallow matching properties.
	 *
	 **/
	private Class<?> not = Void.class;

	/**
	 * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If more than one match the derived schemas, a validation error will occur.
	 *
	 **/
	private Class<?>[] oneOf = {};

	/**
	 * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If any match, the schema will be considered valid.
	 *
	 **/
	private Class<?>[] anyOf = {};

	/**
	 * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If all match, the schema will be considered valid
	 *
	 **/
	private Class<?>[] allOf = {};

	/**
	 * The name of the schema or property.
	 *
	 **/
	private String name = "";

	/**
	 * A title to explain the purpose of the schema.
	 *
	 **/
	private String title = "";

	/**
	 * Constrains a value such that when divided by the multipleOf, the remainder must be an integer.  Ignored if the value is 0.
	 *
	 **/
	private double multipleOf = 0;

	/**
	 * Sets the maximum numeric value for a property.  Ignored if the value is an empty string.
	 *
	 **/
	private String maximum = "";

	/**
	 * if true, makes the maximum value exclusive, or a less-than criteria.
	 *
	 **/
	private boolean exclusiveMaximum;

	/**
	 * Sets the minimum numeric value for a property.  Ignored if the value is an empty string or not a number.
	 *
	 **/
	private String minimum = "";

	/**
	 * If true, makes the minimum value exclusive, or a greater-than criteria.
	 *
	 **/
	private boolean exclusiveMinimum;

	/**
	 * Sets the maximum length of a string value.  Ignored if the value is negative.
	 *
	 **/
	private int maxLength = Integer.MAX_VALUE;

	/**
	 * Sets the minimum length of a string value.  Ignored if the value is negative.
	 *
	 **/
	private int minLength = 0;

	/**
	 * A pattern that the value must satisfy. Ignored if the value is an empty string.
	 *
	 **/
	private String pattern = "";

	/**
	 * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
	 *
	 **/
	private int maxProperties = 0;

	/**
	 * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
	 *
	 **/
	private int minProperties = 0;

	/**
	 * Allows multiple properties in an object to be marked as required.
	 *
	 **/
	private String[] requiredProperties = {};

	/**
	 * Mandates that the annotated item is required or not.
	 *
	 **/
	private boolean required;

	/**
	 * A description of the schema.
	 *
	 **/
	private String description = "";

	/**
	 * Provides an optional override for the format.  If a consumer is unaware of the meaning of the format, they shall fall back to using the basic type without format.  For example, if \&quot;type: integer, format: int128\&quot; were used to designate a very large integer, most consumers will not understand how to handle it, and fall back to simply \&quot;type: integer\&quot;
	 *
	 **/
	private String format = "";

	/**
	 * References a schema definition in an external OpenAPI document.
	 *
	 **/
	private String ref = "";

	/**
	 * If true, designates a value as possibly null.
	 *
	 **/
	private boolean nullable;

	/**
	 * Sets whether the value should only be read during a response but not read to during a request.
	 *
	 * @deprecated As of swagger-core 2.0.0
	 *
	 *
	 **/
	@Deprecated
	private boolean readOnly;

	/**
	 * Sets whether a value should only be written to during a request but not returned during a response.
	 *
	 * @deprecated As of  swagger-core  2.0.0
	 *
	 **/
	@Deprecated
	private boolean writeOnly;

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
	 **/
	private String example = "";

	/**
	 * Additional external documentation for this schema.
	 *
	 **/
	private ExternalDocumentation externalDocs = ExternalDocumentationBuilder.builder().build();

	/**
	 * Specifies that a schema is deprecated and should be transitioned out of usage.
	 *
	 **/
	private boolean deprecated;

	/**
	 * Provides an override for the basic type of the schema.  Must be a valid type per the OpenAPI Specification.
	 *
	 **/
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
	 * @since swagger-core 2.1.0
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


	private SchemaBuilder() {
	}

	public static SchemaBuilder builder() {
		return new SchemaBuilder();
	}

	public SchemaBuilder implementation(Class<?> implementation) {
		this.implementation = implementation;
		return this;
	}

	public SchemaBuilder not(Class<?> not) {
		this.not = not;
		return this;
	}

	public SchemaBuilder oneOf(Class<?>[] oneOf) {
		this.oneOf = oneOf;
		return this;
	}

	public SchemaBuilder anyOf(Class<?>[] anyOf) {
		this.anyOf = anyOf;
		return this;
	}

	public SchemaBuilder allOf(Class<?>[] allOf) {
		this.allOf = allOf;
		return this;
	}

	public SchemaBuilder name(String name) {
		this.name = name;
		return this;
	}

	public SchemaBuilder title(String title) {
		this.title = title;
		return this;
	}

	public SchemaBuilder multipleOf(double multipleOf) {
		this.multipleOf = multipleOf;
		return this;
	}

	public SchemaBuilder maximum(String maximum) {
		this.maximum = maximum;
		return this;
	}

	public SchemaBuilder exclusiveMaximum(boolean exclusiveMaximum) {
		this.exclusiveMaximum = exclusiveMaximum;
		return this;
	}

	public SchemaBuilder minimum(String minimum) {
		this.minimum = minimum;
		return this;
	}

	public SchemaBuilder exclusiveMinimum(boolean exclusiveMinimum) {
		this.exclusiveMinimum = exclusiveMinimum;
		return this;
	}

	public SchemaBuilder maxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	public SchemaBuilder minLength(int minLength) {
		this.minLength = minLength;
		return this;
	}

	public SchemaBuilder pattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public SchemaBuilder maxProperties(int maxProperties) {
		this.maxProperties = maxProperties;
		return this;
	}

	public SchemaBuilder minProperties(int minProperties) {
		this.minProperties = minProperties;
		return this;
	}

	public SchemaBuilder requiredProperties(String[] requiredProperties) {
		this.requiredProperties = requiredProperties;
		return this;
	}

	public SchemaBuilder required(boolean required) {
		this.required = required;
		return this;
	}

	public SchemaBuilder description(String description) {
		this.description = description;
		return this;
	}

	public SchemaBuilder format(String format) {
		this.format = format;
		return this;
	}

	public SchemaBuilder ref(String ref) {
		this.ref = ref;
		return this;
	}

	public SchemaBuilder nullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	public SchemaBuilder readOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	public SchemaBuilder writeOnly(boolean writeOnly) {
		this.writeOnly = writeOnly;
		return this;
	}

	public SchemaBuilder accessMode(AccessMode accessMode) {
		this.accessMode = accessMode;
		return this;
	}

	public SchemaBuilder example(String example) {
		this.example = example;
		return this;
	}

	public SchemaBuilder externalDocs(ExternalDocumentationBuilder externalDocumentationBuilder) {
		this.externalDocs = externalDocumentationBuilder.build();
		return this;
	}

	public SchemaBuilder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	public SchemaBuilder type(String type) {
		this.type = type;
		return this;
	}

	public SchemaBuilder allowableValues(String[] allowableValues) {
		this.allowableValues = allowableValues;
		return this;
	}

	public SchemaBuilder defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public SchemaBuilder discriminatorProperty(String discriminatorProperty) {
		this.discriminatorProperty = discriminatorProperty;
		return this;
	}

	public SchemaBuilder discriminatorMapping(DiscriminatorMappingBuilder discriminatorMappingBuilder) {
		this.discriminatorMapping = ArrayUtils.add(this.discriminatorMapping, discriminatorMappingBuilder.build());
		return this;
	}

	public SchemaBuilder hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public SchemaBuilder enumAsRef(boolean enumAsRef) {
		this.enumAsRef = enumAsRef;
		return this;
	}

	public SchemaBuilder subTypes(Class<?>[] subTypes) {
		this.subTypes = subTypes;
		return this;
	}

	public SchemaBuilder extensions(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	public Schema build() {
		Schema schema = new Schema() {
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
				return readOnly;
			}

			@Override
			public boolean writeOnly() {
				return writeOnly;
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
		return schema;
	}
}
