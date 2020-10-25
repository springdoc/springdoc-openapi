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

package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Content builder.
 */
public class ContentBuilder {

	/**
	 * The media type that this object applies to.
	 *
	 */
	private String mediaType = "";

	/**
	 * An array of examples used to show the use of the associated schema.
	 *
	 */
	private ExampleObject[] examples = {};

	/**
	 * The schema defining the type used for the content.
	 *
	 */
	private Schema schema = SchemaBuilder.builder().build();

	/**
	 * The schema of the array that defines the type used for the content.
	 *
	 */
	private ArraySchema array = ArraySchemaBuilder.builder().build();

	/**
	 * An array of encodings
	 * The key, being the property name, MUST exist in the schema as a property.
	 *
	 */
	private Encoding[] encodings = {};

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};


	/**
	 * Instantiates a new Content builder.
	 */
	private ContentBuilder() {
	}

	/**
	 * Builder content builder.
	 *
	 * @return the content builder
	 */
	public static ContentBuilder builder() {
		return new ContentBuilder();
	}

	/**
	 * Media type content builder.
	 *
	 * @param mediaType the media type
	 * @return the content builder
	 */
	public ContentBuilder mediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	/**
	 * Example content builder.
	 *
	 * @param exampleObjectBuilder the example object builder
	 * @return the content builder
	 */
	public ContentBuilder example(ExampleObjectBuilder exampleObjectBuilder) {
		this.examples =  ArrayUtils.add( this.examples, exampleObjectBuilder.build());
		return this;
	}

	/**
	 * Schema content builder.
	 *
	 * @param schemaBuilder the schema builder
	 * @return the content builder
	 */
	public ContentBuilder schema(SchemaBuilder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	/**
	 * Array content builder.
	 *
	 * @param arraySchemaBuilder the array schema builder
	 * @return the content builder
	 */
	public ContentBuilder array(ArraySchemaBuilder arraySchemaBuilder) {
		this.array = arraySchemaBuilder.build();
		return this;
	}

	/**
	 * Encoding content builder.
	 *
	 * @param encodingBuilder the encoding builder
	 * @return the content builder
	 */
	public ContentBuilder encoding(EncodingBuilder encodingBuilder) {
		this.encodings = ArrayUtils.add( this.encodings, encodingBuilder.build());
		return this;
	}

	/**
	 * Extension content builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the content builder
	 */
	public ContentBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build content.
	 *
	 * @return the content
	 */
	public Content build() {
		Content content = new Content(){

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String mediaType() {
				return mediaType;
			}

			@Override
			public ExampleObject[] examples() {
				return examples;
			}

			@Override
			public Schema schema() {
				return schema;
			}

			@Override
			public ArraySchema array() {
				return array;
			}

			@Override
			public Encoding[] encoding() {
				return encodings;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
		return content;
	}
}
