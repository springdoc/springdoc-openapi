package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;

public class HeaderBuilder {

	/**
	 * Required: The name of the header. The name is only used as the key to store this header in a map.
	 *
	 **/
	private String name;

	/**
	 * Additional description data to provide on the purpose of the header
	 *
	 **/
	private String description="";

	/**
	 * The schema defining the type used for the header. Ignored if the properties content or array are specified.
	 *
	 **/
	private Schema schema=SchemaBuilder.builder().build();

	/**
	 * Determines whether this header is mandatory. The property may be included and its default value is false.
	 *
	 **/
	private boolean required;

	/**
	 * Specifies that a header is deprecated and should be transitioned out of usage.
	 *
	 **/
	private boolean deprecated;

	/**
	 * A reference to a header defined in components headers.
	 * @since swagger-core 2.0.3
	 **/
	private String ref = "";

	private HeaderBuilder() {
	}

	public static HeaderBuilder builder() {
		return new HeaderBuilder();
	}

	public HeaderBuilder name(String name) {
		this.name = name;
		return this;
	}

	public HeaderBuilder description(String description) {
		this.description = description;
		return this;
	}

	public HeaderBuilder schema(SchemaBuilder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	public HeaderBuilder required(boolean required) {
		this.required = required;
		return this;
	}

	public HeaderBuilder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	public HeaderBuilder ref(String ref) {
		this.ref = ref;
		return this;
	}

	public Header build() {
		Header header = new Header() {

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
		};
		return header;
	}
}

