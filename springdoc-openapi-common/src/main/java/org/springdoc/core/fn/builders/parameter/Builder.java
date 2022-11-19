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

package org.springdoc.core.fn.builders.parameter;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Parameter builder.
 * @author bnasslahsen
 */
public class Builder {
	/**
	 * The name of the parameter.
	 *
	 */
	private String name = "";

	/**
	 * The location of the parameter.  Possible values are "query", "header", "path" or "cookie".  Ignored when empty string.
	 *
	 */
	private ParameterIn in = ParameterIn.DEFAULT;

	/**
	 * Additional description data to provide on the purpose of the parameter
	 *
	 */
	private String description = "";

	/**
	 * Determines whether this parameter is mandatory. If the parameter location is "path", this property is required and its value must be true. Otherwise, the property may be included and its default value is false.
	 *
	 */
	private boolean required;

	/**
	 * Specifies that a parameter is deprecated and should be transitioned out of usage.
	 *
	 */
	private boolean deprecated;

	/**
	 * When true, allows sending an empty value.  If false, the parameter will be considered \&quot;null\&quot; if no value is present.  This may create validation errors when the parameter is required.
	 *
	 */
	private boolean allowEmptyValue;

	/**
	 * Describes how the parameter value will be serialized depending on the type of the parameter value. Default values (based on value of in): for query - form; for path - simple; for header - simple; for cookie - form.  Ignored if the properties content or array are specified.
	 *
	 */
	private ParameterStyle style = ParameterStyle.DEFAULT;

	/**
	 * When this is true, parameter values of type array or object generate separate parameters for each value of the array or key-value pair of the map. For other types of parameters this property has no effect. When style is form, the default value is true. For all other styles, the default value is false.  Ignored if the properties content or array are specified.
	 *
	 */
	private Explode explode = Explode.DEFAULT;

	/**
	 * Determines whether the parameter value should allow reserved characters, as defined by RFC3986. This property only applies to parameters with an in value of query. The default value is false.  Ignored if the properties content or array are specified.
	 *
	 */
	private boolean allowReserved;

	/**
	 * The schema defining the type used for the parameter.  Ignored if the properties content or array are specified.
	 *
	 */
	private Schema schema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().build();

	/**
	 * The schema of the array that defines this parameter.  Ignored if the property content is specified.
	 *
	 */
	private ArraySchema array = org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder().build();

	/**
	 * The representation of this parameter, for different media types.
	 *
	 */
	private Content[] content = {};

	/**
	 * Allows this parameter to be marked as hidden
	 *
	 */
	private boolean hidden;

	/**
	 * An array of examples  of the schema used to show the use of the associated schema.
	 *
	 */
	private ExampleObject[] examples = {};

	/**
	 * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.  Ignored if the properties examples, content or array are specified.
	 *
	 */
	private String example = "";

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * A reference to a parameter defined in components parameter.
	 *
	 * @since swagger -core 2.0.3
	 */
	private String ref = "";


	/**
	 * Instantiates a new Parameter builder.
	 */
	private Builder() {
	}

	/**
	 * Builder parameter builder.
	 *
	 * @return the parameter builder
	 */
	public static Builder parameterBuilder() {
		return new Builder();
	}

	/**
	 * Name parameter builder.
	 *
	 * @param name the name
	 * @return the parameter builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * In parameter builder.
	 *
	 * @param in the in
	 * @return the parameter builder
	 */
	public Builder in(ParameterIn in) {
		this.in = in;
		return this;
	}

	/**
	 * Description parameter builder.
	 *
	 * @param description the description
	 * @return the parameter builder
	 */
	public Builder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Required parameter builder.
	 *
	 * @param required the required
	 * @return the parameter builder
	 */
	public Builder required(boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * Deprecated parameter builder.
	 *
	 * @param deprecated the deprecated
	 * @return the parameter builder
	 */
	public Builder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	/**
	 * Allow empty value parameter builder.
	 *
	 * @param allowEmptyValue the allow empty value
	 * @return the parameter builder
	 */
	public Builder allowEmptyValue(boolean allowEmptyValue) {
		this.allowEmptyValue = allowEmptyValue;
		return this;
	}

	/**
	 * Style parameter builder.
	 *
	 * @param style the style
	 * @return the parameter builder
	 */
	public Builder style(ParameterStyle style) {
		this.style = style;
		return this;
	}

	/**
	 * Explode parameter builder.
	 *
	 * @param explode the explode
	 * @return the parameter builder
	 */
	public Builder explode(Explode explode) {
		this.explode = explode;
		return this;
	}

	/**
	 * Allow reserved parameter builder.
	 *
	 * @param allowReserved the allow reserved
	 * @return the parameter builder
	 */
	public Builder allowReserved(boolean allowReserved) {
		this.allowReserved = allowReserved;
		return this;
	}

	/**
	 * Schema parameter builder.
	 *
	 * @param schemaBuilder the schema builder
	 * @return the parameter builder
	 */
	public Builder schema(org.springdoc.core.fn.builders.schema.Builder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	/**
	 * Array parameter builder.
	 *
	 * @param arraySchemaBuilder the array schema builder
	 * @return the parameter builder
	 */
	public Builder array(org.springdoc.core.fn.builders.arrayschema.Builder arraySchemaBuilder) {
		this.array = arraySchemaBuilder.build();
		return this;
	}

	/**
	 * Implementation array parameter builder.
	 *
	 * @param clazz the clazz
	 * @return the parameter builder
	 */
	public Builder implementationArray(Class clazz) {
		this.array = org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder().schema(org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().implementation(clazz)).build();
		return this;
	}

	/**
	 * Implementation parameter builder.
	 *
	 * @param clazz the clazz
	 * @return the parameter builder
	 */
	public Builder implementation(Class clazz) {
		this.schema = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().implementation(clazz).build();
		return this;
	}

	/**
	 * Content parameter builder.
	 *
	 * @param contentBuilder the content builder
	 * @return the parameter builder
	 */
	public Builder content(org.springdoc.core.fn.builders.content.Builder contentBuilder) {
		this.content = ArrayUtils.add(this.content, contentBuilder.build());
		return this;
	}

	/**
	 * Hidden parameter builder.
	 *
	 * @param hidden the hidden
	 * @return the parameter builder
	 */
	public Builder hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	/**
	 * Examples parameter builder.
	 *
	 * @param exampleObjectBuilder the example object builder
	 * @return the parameter builder
	 */
	public Builder examples(org.springdoc.core.fn.builders.exampleobject.Builder exampleObjectBuilder) {
		this.examples = ArrayUtils.add(this.examples, exampleObjectBuilder.build());
		return this;
	}

	/**
	 * Example parameter builder.
	 *
	 * @param example the example
	 * @return the parameter builder
	 */
	public Builder example(String example) {
		this.example = example;
		return this;
	}

	/**
	 * Extensions parameter builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the parameter builder
	 */
	public Builder extensions(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Ref parameter builder.
	 *
	 * @param ref the ref
	 * @return the parameter builder
	 */
	public Builder ref(String ref) {
		this.ref = ref;
		return this;
	}

	/**
	 * Build parameter.
	 *
	 * @return the parameter
	 */
	public Parameter build() {
		return new Parameter() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public ParameterIn in() {
				return in;
			}

			@Override
			public String description() {
				return description;
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
			public boolean allowEmptyValue() {
				return allowEmptyValue;
			}

			@Override
			public ParameterStyle style() {
				return style;
			}

			@Override
			public Explode explode() {
				return explode;
			}

			@Override
			public boolean allowReserved() {
				return allowReserved;
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
			public Content[] content() {
				return content;
			}

			@Override
			public boolean hidden() {
				return hidden;
			}

			@Override
			public ExampleObject[] examples() {
				return examples;
			}

			@Override
			public String example() {
				return example;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}

			@Override
			public String ref() {
				return ref;
			}
		};
	}
}

