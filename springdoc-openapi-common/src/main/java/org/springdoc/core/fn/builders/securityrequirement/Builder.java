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

package org.springdoc.core.fn.builders.securityrequirement;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * The type Security requirement builder.
 * @author bnasslahsen
 */
public class Builder {
	/**
	 * This name must correspond to a declared SecurityRequirement.
	 *
	 */
	private String name;

	/**
	 * If the security scheme is of type "oauth2" or "openIdConnect", then the value is a list of scope names required for the execution.
	 * For other security scheme types, the array must be empty.
	 *
	 */
	private String[] scopes = {};


	/**
	 * Instantiates a new Security requirement builder.
	 */
	private Builder() {
	}

	/**
	 * Builder security requirement builder.
	 *
	 * @return the security requirement builder
	 */
	public static Builder securityRequirementBuilder() {
		return new Builder();
	}

	/**
	 * Name security requirement builder.
	 *
	 * @param name the name
	 * @return the security requirement builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Scopes security requirement builder.
	 *
	 * @param scopes the scopes
	 * @return the security requirement builder
	 */
	public Builder scopes(String[] scopes) {
		this.scopes = scopes;
		return this;
	}

	/**
	 * Build security requirement.
	 *
	 * @return the security requirement
	 */
	public SecurityRequirement build() {
		return new SecurityRequirement() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String[] scopes() {
				return scopes;
			}
		};
	}
}
