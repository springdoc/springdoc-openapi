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

package org.springdoc.core.fn.builders.externaldocumentation;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type External documentation builder.
 *
 * @author bnasslahsen
 */
public class Builder {

	/**
	 * A short description of the target documentation.
	 */
	private String description = "";

	/**
	 * The URL for the target documentation. Value must be in the format of a URL.
	 */
	private String url = "";

	/**
	 * The list of optional extensions
	 */
	private Extension[] extensions = {};


	/**
	 * Instantiates a new External documentation builder.
	 */
	private Builder() {
	}

	/**
	 * An external documentation external documentation builder.
	 *
	 * @return the external documentation builder
	 */
	public static Builder externalDocumentationBuilder() {
		return new Builder();
	}

	/**
	 * Description external documentation builder.
	 *
	 * @param description the description
	 * @return the external documentation builder
	 */
	public Builder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Url external documentation builder.
	 *
	 * @param url the url
	 * @return the external documentation builder
	 */
	public Builder url(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Extensions external documentation builder.
	 *
	 * @param extensionBuilder the extensions
	 * @return the external documentation builder
	 */
	public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build external documentation.
	 *
	 * @return the external documentation
	 */
	public ExternalDocumentation build() {
		return new ExternalDocumentation() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public String url() {
				return url;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
	}
}
