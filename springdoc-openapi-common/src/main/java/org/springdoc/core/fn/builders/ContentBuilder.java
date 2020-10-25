package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;

public class ContentBuilder {

	/**
	 * The media type that this object applies to.
	 *
	 **/
	private String mediaType = "";

	/**
	 * An array of examples used to show the use of the associated schema.
	 *
	 **/
	private ExampleObject[] examples = {};

	/**
	 * The schema defining the type used for the content.
	 *
	 **/
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


	private ContentBuilder() {
	}

	public static ContentBuilder builder() {
		return new ContentBuilder();
	}

	public ContentBuilder mediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public ContentBuilder example(ExampleObjectBuilder exampleObjectBuilder) {
		this.examples =  ArrayUtils.add( this.examples, exampleObjectBuilder.build());
		return this;
	}

	public ContentBuilder schema(SchemaBuilder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	public ContentBuilder array(ArraySchemaBuilder arraySchemaBuilder) {
		this.array = arraySchemaBuilder.build();
		return this;
	}

	public ContentBuilder encoding(EncodingBuilder encodingBuilder) {
		this.encodings = ArrayUtils.add( this.encodings, encodingBuilder.build());
		return this;
	}

	public ContentBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

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
