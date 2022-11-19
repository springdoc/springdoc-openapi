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

package org.springdoc.core.fn.builders.extensionproperty;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

/**
 * The type Extension property builder.
 * @author bnasslahsen
 */
public class Builder {

	/**
	 * The name of the property.
	 *
	 */
	private String name;

	/**
	 * The value of the property.
	 *
	 */
	private String value;

	/**
	 * If set to true, field `value` will be parsed and serialized as JSON/YAML
	 *
	 */
	private boolean parseValue;

	/**
	 * Instantiates a new Extension property builder.
	 */
	private Builder() {
	}

	/**
	 * An extension property extension property builder.
	 *
	 * @return the extension property builder
	 */
	public static Builder extensionPropertyBuilder() {
		return new Builder();
	}

	/**
	 * Name extension property builder.
	 *
	 * @param name the name
	 * @return the extension property builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Value extension property builder.
	 *
	 * @param value the value
	 * @return the extension property builder
	 */
	public Builder value(String value) {
		this.value = value;
		return this;
	}

	/**
	 * Parse value extension property builder.
	 *
	 * @param parseValue the parse value
	 * @return the extension property builder
	 */
	public Builder parseValue(boolean parseValue) {
		this.parseValue = parseValue;
		return this;
	}

	/**
	 * Build extension property.
	 *
	 * @return the extension property
	 */
	public ExtensionProperty build() {
		return new ExtensionProperty() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String value() {
				return value;
			}

			@Override
			public boolean parseValue() {
				return parseValue;
			}
		};
	}
}
