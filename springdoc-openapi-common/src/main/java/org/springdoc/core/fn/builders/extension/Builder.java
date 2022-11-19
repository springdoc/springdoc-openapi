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

package org.springdoc.core.fn.builders.extension;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Extension builder.
 * @author bnasslahsen
 */
public class Builder {

	/**
	 * An option name for these extensions.
	 *
	 */
	private String name = "";

	/**
	 * The extension properties.
	 *
	 */
	private ExtensionProperty[] properties;

	/**
	 * Instantiates a new Extension builder.
	 */
	private Builder() {
	}

	/**
	 * Builder extension builder.
	 *
	 * @return the extension builder
	 */
	public static Builder extensionBuilder() {
		return new Builder();
	}

	/**
	 * Name extension builder.
	 *
	 * @param name the name
	 * @return the extension builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Properties extension builder.
	 *
	 * @param extensionPropertyBuilder the properties
	 * @return the extension builder
	 */
	public Builder propertie(org.springdoc.core.fn.builders.extensionproperty.Builder extensionPropertyBuilder) {
		this.properties = ArrayUtils.add(this.properties, extensionPropertyBuilder.build());
		return this;
	}

	/**
	 * Build extension.
	 *
	 * @return the extension
	 */
	public Extension build() {
		return new Extension() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public ExtensionProperty[] properties() {
				return properties;
			}
		};
	}
}
