package org.springdoc.core.delelegates;

/**
 * @author bnasslahsen
 */

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

/**
 * Utility class to delegate to another @Parameter annotation.
 * @author daniel-shuy
 */
public class ParameterDelegate implements Parameter {
	private final Parameter parameter;

	/**
	 * Create a delegate to the given Parameter.
	 * @param parameter The Parameter annotation to delegate to.
	 */
	public ParameterDelegate(Parameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return parameter.annotationType();
	}

	@Override
	public String name() {
		return parameter.name();
	}

	@Override
	public ParameterIn in() {
		return parameter.in();
	}

	@Override
	public String description() {
		return parameter.description();
	}

	@Override
	public boolean required() {
		return parameter.required();
	}

	@Override
	public boolean deprecated() {
		return parameter.deprecated();
	}

	@Override
	public boolean allowEmptyValue() {
		return parameter.allowEmptyValue();
	}

	@Override
	public ParameterStyle style() {
		return parameter.style();
	}

	@Override
	public Explode explode() {
		return parameter.explode();
	}

	@Override
	public boolean allowReserved() {
		return parameter.allowReserved();
	}

	@Override
	public Schema schema() {
		return parameter.schema();
	}

	@Override
	public ArraySchema array() {
		return parameter.array();
	}

	@Override
	public Content[] content() {
		return parameter.content();
	}

	@Override
	public boolean hidden() {
		return parameter.hidden();
	}

	@Override
	public ExampleObject[] examples() {
		return parameter.examples();
	}

	@Override
	public String example() {
		return parameter.example();
	}

	@Override
	public Extension[] extensions() {
		return parameter.extensions();
	}

	@Override
	public String ref() {
		return parameter.ref();
	}
}