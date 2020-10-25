package org.springdoc.core.fn.builders;

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

public class ParameterBuilder {
	/**
	 * The name of the parameter.
	 *
	 **/
	private String name = "";

	/**
	 * The location of the parameter.  Possible values are "query", "header", "path" or "cookie".  Ignored when empty string.
	 *
	 **/
	private ParameterIn in = ParameterIn.DEFAULT;

	/**
	 * Additional description data to provide on the purpose of the parameter
	 *
	 **/
	private String description = "";

	/**
	 * Determines whether this parameter is mandatory. If the parameter location is "path", this property is required and its value must be true. Otherwise, the property may be included and its default value is false.
	 *
	 **/
	private boolean required;

	/**
	 * Specifies that a parameter is deprecated and should be transitioned out of usage.
	 *
	 **/
	private boolean deprecated;

	/**
	 * When true, allows sending an empty value.  If false, the parameter will be considered \&quot;null\&quot; if no value is present.  This may create validation errors when the parameter is required.
	 *
	 **/
	private boolean allowEmptyValue;

	/**
	 * Describes how the parameter value will be serialized depending on the type of the parameter value. Default values (based on value of in): for query - form; for path - simple; for header - simple; for cookie - form.  Ignored if the properties content or array are specified.
	 *
	 **/
	private ParameterStyle style = ParameterStyle.DEFAULT;

	/**
	 * When this is true, parameter values of type array or object generate separate parameters for each value of the array or key-value pair of the map. For other types of parameters this property has no effect. When style is form, the default value is true. For all other styles, the default value is false.  Ignored if the properties content or array are specified.
	 *
	 **/
	private Explode explode = Explode.DEFAULT;

	/**
	 * Determines whether the parameter value should allow reserved characters, as defined by RFC3986. This property only applies to parameters with an in value of query. The default value is false.  Ignored if the properties content or array are specified.
	 *
	 **/
	private boolean allowReserved;

	/**
	 * The schema defining the type used for the parameter.  Ignored if the properties content or array are specified.
	 *
	 **/
	private Schema schema = SchemaBuilder.builder().build();

	/**
	 * The schema of the array that defines this parameter.  Ignored if the property content is specified.
	 *
	 */
	private ArraySchema array = ArraySchemaBuilder.builder().build();

	/**
	 * The representation of this parameter, for different media types.
	 *
	 **/
	private Content[] content = {};

	/**
	 * Allows this parameter to be marked as hidden
	 *
	 */
	private boolean hidden;

	/**
	 * An array of examples  of the schema used to show the use of the associated schema.
	 *
	 **/
	private ExampleObject[] examples = {};

	/**
	 * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.  Ignored if the properties examples, content or array are specified.
	 *
	 **/
	private String example = "";

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * A reference to a parameter defined in components parameter.
	 *
	 * @since swagger-core 2.0.3
	 **/
	private String ref = "";


	private ParameterBuilder() {
	}

	public static ParameterBuilder builder() {
		return new ParameterBuilder();
	}

	public ParameterBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ParameterBuilder in(ParameterIn in) {
		this.in = in;
		return this;
	}

	public ParameterBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ParameterBuilder required(boolean required) {
		this.required = required;
		return this;
	}

	public ParameterBuilder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	public ParameterBuilder allowEmptyValue(boolean allowEmptyValue) {
		this.allowEmptyValue = allowEmptyValue;
		return this;
	}

	public ParameterBuilder style(ParameterStyle style) {
		this.style = style;
		return this;
	}

	public ParameterBuilder explode(Explode explode) {
		this.explode = explode;
		return this;
	}

	public ParameterBuilder allowReserved(boolean allowReserved) {
		this.allowReserved = allowReserved;
		return this;
	}

	public ParameterBuilder schema(SchemaBuilder schemaBuilder) {
		this.schema = schemaBuilder.build();
		return this;
	}

	public ParameterBuilder array(ArraySchemaBuilder arraySchemaBuilder) {
		this.array = arraySchemaBuilder.build();
		return this;
	}

	public ParameterBuilder implementationArray(Class clazz) {
		this.array = ArraySchemaBuilder.builder().schema(SchemaBuilder.builder().implementation(clazz)).build();
		return this;
	}

	public ParameterBuilder implementation(Class clazz) {
		this.schema =  SchemaBuilder.builder().implementation(clazz).build();
		return this;
	}

	public ParameterBuilder content(ContentBuilder contentBuilder) {
		this.content = ArrayUtils.add( this.content, contentBuilder.build());
		return this;
	}

	public ParameterBuilder hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public ParameterBuilder examples(ExampleObjectBuilder exampleObjectBuilder) {
		this.examples = ArrayUtils.add( this.examples, exampleObjectBuilder.build());
		return this;
	}

	public ParameterBuilder example(String example) {
		this.example = example;
		return this;
	}

	public ParameterBuilder extensions(ExtensionBuilder extensionBuilder) {
		this.extensions =  ArrayUtils.add( this.extensions, extensionBuilder.build() );
		return this;
	}

	public ParameterBuilder ref(String ref) {
		this.ref = ref;
		return this;
	}

	public Parameter build() {
		Parameter parameter = new Parameter(){

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
		return parameter;
	}
}

