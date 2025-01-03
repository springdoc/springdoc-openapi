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

package org.springdoc.core.fn.builders.content;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.DependentSchema;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Content builder.
 *
 * @author bnasslahsen
 */
public class Builder {

	/**
	 * The schema properties defined for schema provided in @Schema
	 */
	private final Schema additionalPropertiesSchema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The Additional properties array schema.
	 */
	private final ArraySchema additionalPropertiesArraySchema = org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder().build();

	/**
	 * The schema properties defined for schema provided in @Schema
	 */
	private final SchemaProperty[] schemaProperties = {};

	/**
	 * The media type that this object applies to.
	 */
	private String mediaType = "";

	/**
	 * An array of examples used to show the use of the associated schema.
	 */
	private ExampleObject[] examples = {};

	/**
	 * The schema defining the type used for the content.
	 */
	private Schema schema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The schema of the array that defines the type used for the content.
	 */
	private ArraySchema array = org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder().build();

	/**
	 * An array of encodings
	 * The key, being the property name, MUST exist in the schema as a property.
	 */
	private Encoding[] encodings = {};

	/**
	 * The list of optional extensions
	 */
	private Extension[] extensions = {};

	/**
	 * The Dependent schemas.
	 */
	private DependentSchema[] dependentSchemas = {};

	/**
	 * The Content schem.
	 */
	private Schema contentSchema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The Property names.
	 */
	private Schema propertyNames = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The If.
	 */
	private Schema _if = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The Then.
	 */
	private Schema _then = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The Else.
	 */
	private Schema _else = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The Not.
	 */
	private Schema not = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The One of.
	 */
	private Schema[] oneOf = {};

	/**
	 * The Any of.
	 */
	private Schema[] anyOf = {};

	/**
	 * The All of.
	 */
	private Schema[] allOf ={};

	/**
	 * Instantiates a new Content builder.
	 */
	private Builder() {
	}

	/**
	 * Builder content builder.
	 *
	 * @return the content builder
	 */
	public static Builder contentBuilder() {
		return new Builder();
	}

	/**
	 * Media type content builder.
	 *
	 * @param mediaType the media type
	 * @return the content builder
	 */
	public Builder mediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	/**
	 * Example content builder.
	 *
	 * @param exampleObjectBuilder the example object builder
	 * @return the content builder
	 */
	public Builder example(org.springdoc.core.fn.builders.exampleobject.Builder exampleObjectBuilder) {
		this.examples = ArrayUtils.add(this.examples, exampleObjectBuilder.build());
		return this;
	}

	/**
	 * Schema content builder.
	 *
	 * @param schemaBuilder the schema builder
	 * @return the content builder
	 */
	public Builder schema(org.springdoc.core.fn.builders.schema.Builder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	/**
	 * Array content builder.
	 *
	 * @param arraySchemaBuilder the array schema builder
	 * @return the content builder
	 */
	public Builder array(org.springdoc.core.fn.builders.arrayschema.Builder arraySchemaBuilder) {
		this.array = arraySchemaBuilder.build();
		return this;
	}

	/**
	 * Encoding content builder.
	 *
	 * @param encodingBuilder the encoding builder
	 * @return the content builder
	 */
	public Builder encoding(org.springdoc.core.fn.builders.encoding.Builder encodingBuilder) {
		this.encodings = ArrayUtils.add(this.encodings, encodingBuilder.build());
		return this;
	}

	/**
	 * Extension content builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the content builder
	 */
	public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build content.
	 *
	 * @return the content
	 */
	public Content build() {
		return new Content() {

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
			public SchemaProperty[] schemaProperties() {
				return schemaProperties;
			}

			@Override
			public Schema additionalPropertiesSchema() {
				return additionalPropertiesSchema;
			}

			@Override
			public ArraySchema additionalPropertiesArraySchema() {
				return additionalPropertiesArraySchema;
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

			@Override
			public DependentSchema[] dependentSchemas() {
				return new DependentSchema[0];
			}

			@Override
			public Schema contentSchema() {
				return contentSchema;
			}

			@Override
			public Schema propertyNames() {
				return propertyNames;
			}

			@Override
			public Schema _if() {
				return _if;
			}

			@Override
			public Schema _then() {
				return _then;
			}

			@Override
			public Schema _else() {
				return _else;
			}

			@Override
			public Schema not() {
				return not;
			}

			@Override
			public Schema[] oneOf() {
				return oneOf;
			}

			@Override
			public Schema[] anyOf() {
				return anyOf;
			}

			@Override
			public Schema[] allOf() {
				return allOf;
			}
		};
	}
}
