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

package org.springdoc.core.fn.builders.discriminatormapping;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;

/**
 * The type Discriminator mapping builder.
 *
 * @author bnasslahsen
 */
public class Builder {

	/**
	 * The property value that will be mapped to a Schema
	 */
	private String value = "";

	/**
	 * The schema that is being mapped to a property value
	 */
	private Class<?> schema = Void.class;

	/**
	 * The Extensions.
	 */
	private Extension[] extensions = {};

	/**
	 * Instantiates a new Discriminator mapping builder.
	 */
	private Builder() {
	}

	/**
	 * A discriminator mapping builde discriminator mapping builder.
	 *
	 * @return the discriminator mapping builder
	 */
	public static Builder discriminatorMappingBuilder() {
		return new Builder();
	}

	/**
	 * Value discriminator mapping builder.
	 *
	 * @param value the value
	 * @return the discriminator mapping builder
	 */
	public Builder value(String value) {
		this.value = value;
		return this;
	}

	/**
	 * Schema discriminator mapping builder.
	 *
	 * @param schema the schema
	 * @return the discriminator mapping builder
	 */
	public Builder schema(Class<?> schema) {
		this.schema = schema;
		return this;
	}

	/**
	 * Build discriminator mapping.
	 *
	 * @return the discriminator mapping
	 */
	public DiscriminatorMapping build() {
		return new DiscriminatorMapping() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String value() {
				return value;
			}

			@Override
			public Class<?> schema() {
				return schema;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
	}

}
