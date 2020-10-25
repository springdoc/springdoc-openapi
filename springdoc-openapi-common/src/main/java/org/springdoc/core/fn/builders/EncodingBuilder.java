package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Encoding;
import org.apache.commons.lang3.ArrayUtils;

public class EncodingBuilder {

	/**
	 * The name of this encoding object instance.
	 * This property is a key in encoding map of MediaType object and
	 * MUST exist in a schema as a property.
	 *
	 **/
	private String name = "";

	/**
	 * The Content-Type for encoding a specific property.
	 *
	 **/
	private String contentType = "";

	/**
	 * Describes how a specific property value will be serialized depending on its type
	 *
	 **/
	private String style = "";

	/**
	 * When this is true, property values of type array or object generate separate parameters for each value of the array,
	 * or key-value-pair of the map.
	 *
	 **/
	private boolean explode;

	/**
	 * Determines whether the parameter value SHOULD allow reserved characters,
	 * as defined by RFC3986 to be included without percent-encoding.
	 *
	 **/
	private boolean allowReserved;

	/**
	 * An array of header objects
	 *
	 */
	private Header[] headers = {};

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};


	private EncodingBuilder() {
	}

	public static EncodingBuilder builder() {
		return new EncodingBuilder();
	}

	public EncodingBuilder name(String name) {
		this.name = name;
		return this;
	}

	public EncodingBuilder contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public EncodingBuilder style(String style) {
		this.style = style;
		return this;
	}

	public EncodingBuilder explode(boolean explode) {
		this.explode = explode;
		return this;
	}

	public EncodingBuilder allowReserved(boolean allowReserved) {
		this.allowReserved = allowReserved;
		return this;
	}

	public EncodingBuilder headers(HeaderBuilder headerBuilder) {
		this.headers = ArrayUtils.add( this.headers, headerBuilder.build());
		return this;
	}

	public EncodingBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

	public Encoding build() {
		Encoding encoding = new Encoding(){

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String contentType() {
				return contentType;
			}

			@Override
			public String style() {
				return style;
			}

			@Override
			public boolean explode() {
				return explode;
			}

			@Override
			public boolean allowReserved() {
				return allowReserved;
			}

			@Override
			public Header[] headers() {
				return headers;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
		return encoding;
	}
}
