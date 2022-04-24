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

package org.springdoc.core.fn.builders.linkparameter;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.links.LinkParameter;

/**
 * The type Link parameter builder.
 * @author bnasslahsen
 */
public class Builder {
	/**
	 * The name of this link parameter.
	 *
	 */
	private String name = "";

	/**
	 * A constant or an expression to be evaluated and passed to the linked operation.
	 *
	 */
	private String expression = "";


	/**
	 * Instantiates a new Link parameter builder.
	 */
	private Builder() {
	}

	/**
	 * Builder link parameter builder.
	 *
	 * @return the link parameter builder
	 */
	public static Builder linkParameterBuilder() {
		return new Builder();
	}

	/**
	 * Name link parameter builder.
	 *
	 * @param name the name
	 * @return the link parameter builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Expression link parameter builder.
	 *
	 * @param expression the expression
	 * @return the link parameter builder
	 */
	public Builder expression(String expression) {
		this.expression = expression;
		return this;
	}

	/**
	 * Build link parameter.
	 *
	 * @return the link parameter
	 */
	public LinkParameter build() {
		return new LinkParameter() {

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
	}
}

