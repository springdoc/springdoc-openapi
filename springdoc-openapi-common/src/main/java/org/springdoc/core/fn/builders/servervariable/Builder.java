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

package org.springdoc.core.fn.builders.servervariable;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Server variable builder.
 * @author bnasslahsen
 */
public class Builder {
	/**
	 * Required.  The name of this variable.
	 *
	 */
	private String name;

	/**
	 * An array of allowable values for this variable.  This field map to the enum property in the OAS schema.
	 *
	 * @return String array of allowableValues
	 */
	private String[] allowableValues = {};

	/**
	 * Required.  The default value of this variable.
	 *
	 */
	private String defaultValue;

	/**
	 * An optional description for the server variable.
	 *
	 */
	private String description = "";

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * Instantiates a new Server variable builder.
	 */
	private Builder() {
	}

	/**
	 * Builder server variable builder.
	 *
	 * @return the server variable builder
	 */
	public static Builder serverVariableBuilder() {
		return new Builder();
	}

	/**
	 * Name server variable builder.
	 *
	 * @param name the name
	 * @return the server variable builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Allowable values server variable builder.
	 *
	 * @param allowableValues the allowable values
	 * @return the server variable builder
	 */
	public Builder allowableValues(String[] allowableValues) {
		this.allowableValues = allowableValues;
		return this;
	}

	/**
	 * Default value server variable builder.
	 *
	 * @param defaultValue the default value
	 * @return the server variable builder
	 */
	public Builder defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	/**
	 * Description server variable builder.
	 *
	 * @param description the description
	 * @return the server variable builder
	 */
	public Builder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Extension server variable builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the server variable builder
	 */
	public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build server variable.
	 *
	 * @return the server variable
	 */
	public ServerVariable build() {
		return new ServerVariable() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String[] allowableValues() {
				return allowableValues;
			}

			@Override
			public String defaultValue() {
				return defaultValue;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
	}
}
