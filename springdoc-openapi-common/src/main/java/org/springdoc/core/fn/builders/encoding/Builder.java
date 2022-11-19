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

package org.springdoc.core.fn.builders.encoding;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Encoding;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Encoding builder.
 * @author bnasslahsen
 */
public class Builder {

	/**
	 * The name of this encoding object instance.
	 * This property is a key in encoding map of MediaType object and
	 * MUST exist in a schema as a property.
	 *
	 */
	private String name = "";

	/**
	 * The Content-Type for encoding a specific property.
	 *
	 */
	private String contentType = "";

	/**
	 * Describes how a specific property value will be serialized depending on its type
	 *
	 */
	private String style = "";

	/**
	 * When this is true, property values of type array or object generate separate parameters for each value of the array,
	 * or key-value-pair of the map.
	 *
	 */
	private boolean explode;

	/**
	 * Determines whether the parameter value SHOULD allow reserved characters,
	 * as defined by RFC3986 to be included without percent-encoding.
	 *
	 */
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


	/**
	 * Instantiates a new Encoding builder.
	 */
	private Builder() {
	}

	/**
	 * Builder encoding builder.
	 *
	 * @return the encoding builder
	 */
	public static Builder encodingBuilder() {
		return new Builder();
	}

	/**
	 * Name encoding builder.
	 *
	 * @param name the name
	 * @return the encoding builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Content type encoding builder.
	 *
	 * @param contentType the content type
	 * @return the encoding builder
	 */
	public Builder contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Style encoding builder.
	 *
	 * @param style the style
	 * @return the encoding builder
	 */
	public Builder style(String style) {
		this.style = style;
		return this;
	}

	/**
	 * Explode encoding builder.
	 *
	 * @param explode the explode
	 * @return the encoding builder
	 */
	public Builder explode(boolean explode) {
		this.explode = explode;
		return this;
	}

	/**
	 * Allow reserved encoding builder.
	 *
	 * @param allowReserved the allow reserved
	 * @return the encoding builder
	 */
	public Builder allowReserved(boolean allowReserved) {
		this.allowReserved = allowReserved;
		return this;
	}

	/**
	 * Headers encoding builder.
	 *
	 * @param headerBuilder the header builder
	 * @return the encoding builder
	 */
	public Builder headers(org.springdoc.core.fn.builders.header.Builder headerBuilder) {
		this.headers = ArrayUtils.add(this.headers, headerBuilder.build());
		return this;
	}

	/**
	 * Extension encoding builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the encoding builder
	 */
	public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build encoding.
	 *
	 * @return the encoding
	 */
	public Encoding build() {
		return new Encoding() {

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
	}
}
