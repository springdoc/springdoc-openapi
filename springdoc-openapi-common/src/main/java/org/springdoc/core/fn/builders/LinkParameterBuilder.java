package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.links.LinkParameter;

public class LinkParameterBuilder {
	/**
	 * The name of this link parameter.
	 *
	 **/
	private String name = "";

	/**
	 * A constant or an expression to be evaluated and passed to the linked operation.
	 *
	 **/
	private String expression = "";


	private LinkParameterBuilder() {
	}

	public static LinkParameterBuilder builder() {
		return new LinkParameterBuilder();
	}

	public LinkParameterBuilder name(String name) {
		this.name = name;
		return this;
	}

	public LinkParameterBuilder expression(String expression) {
		this.expression = expression;
		return this;
	}

	public LinkParameter build() {
		LinkParameter linkParameter = new LinkParameter() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String expression() {
				return expression;
			}
		};

		return linkParameter;
	}
}

