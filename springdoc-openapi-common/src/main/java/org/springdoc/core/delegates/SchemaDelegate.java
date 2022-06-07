package org.springdoc.core.delegates;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Utility class to delegate to another @Schema annotation.
 * @author daniel-shuy
 */
public class SchemaDelegate implements Schema {
	private final Schema schema;

	/**
	 * Create a delegate to the given Schema.
	 * @param schema The Schema annotation to delegate to.
	 */
	public SchemaDelegate(Schema schema) {
		this.schema = schema;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return schema.annotationType();
	}

	@Override
	public Class<?> implementation() {
		return schema.implementation();
	}

	@Override
	public Class<?> not() {
		return schema.not();
	}

	@Override
	public Class<?>[] oneOf() {
		return schema.oneOf();
	}

	@Override
	public Class<?>[] anyOf() {
		return schema.anyOf();
	}

	@Override
	public Class<?>[] allOf() {
		return schema.allOf();
	}

	@Override
	public String name() {
		return schema.name();
	}

	@Override
	public String title() {
		return schema.title();
	}

	@Override
	public double multipleOf() {
		return schema.multipleOf();
	}

	@Override
	public String maximum() {
		return schema.maximum();
	}

	@Override
	public boolean exclusiveMaximum() {
		return schema.exclusiveMaximum();
	}

	@Override
	public String minimum() {
		return schema.minimum();
	}

	@Override
	public boolean exclusiveMinimum() {
		return schema.exclusiveMinimum();
	}

	@Override
	public int maxLength() {
		return schema.maxLength();
	}

	@Override
	public int minLength() {
		return schema.minLength();
	}

	@Override
	public String pattern() {
		return schema.pattern();
	}

	@Override
	public int maxProperties() {
		return schema.maxProperties();
	}

	@Override
	public int minProperties() {
		return schema.minProperties();
	}

	@Override
	public String[] requiredProperties() {
		return schema.requiredProperties();
	}

	@Override
	public boolean required() {
		return schema.required();
	}

	@Override
	public String description() {
		return schema.description();
	}

	@Override
	public String format() {
		return schema.format();
	}

	@Override
	public String ref() {
		return schema.ref();
	}

	@Override
	public boolean nullable() {
		return schema.nullable();
	}

	@Override
	public boolean readOnly() {
		return schema.readOnly();
	}

	@Override
	public boolean writeOnly() {
		return schema.writeOnly();
	}

	@Override
	public AccessMode accessMode() {
		return schema.accessMode();
	}

	@Override
	public String example() {
		return schema.example();
	}

	@Override
	public ExternalDocumentation externalDocs() {
		return schema.externalDocs();
	}

	@Override
	public boolean deprecated() {
		return schema.deprecated();
	}

	@Override
	public String type() {
		return schema.type();
	}

	@Override
	public String[] allowableValues() {
		return schema.allowableValues();
	}

	@Override
	public String defaultValue() {
		return schema.defaultValue();
	}

	@Override
	public String discriminatorProperty() {
		return schema.discriminatorProperty();
	}

	@Override
	public DiscriminatorMapping[] discriminatorMapping() {
		return schema.discriminatorMapping();
	}

	@Override
	public boolean hidden() {
		return schema.hidden();
	}

	@Override
	public boolean enumAsRef() {
		return schema.enumAsRef();
	}

	@Override
	public Class<?>[] subTypes() {
		return schema.subTypes();
	}

	@Override
	public Extension[] extensions() {
		return schema.extensions();
	}

	@Override
	public AdditionalPropertiesValue additionalProperties() {
		return schema.additionalProperties();
	}
}
