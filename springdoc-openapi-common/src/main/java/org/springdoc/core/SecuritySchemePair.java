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

package org.springdoc.core;

import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * The type Security scheme pair.
 * @author bnasslahsen
 */
class SecuritySchemePair {

	/**
	 * The Key.
	 */
	private final String key;

	/**
	 * The Security scheme.
	 */
	private final SecurityScheme securityScheme;

	/**
	 * Instantiates a new Security scheme pair.
	 *
	 * @param key the key
	 * @param securityScheme the security scheme
	 */
	public SecuritySchemePair(String key, SecurityScheme securityScheme) {
		super();
		this.key = key;
		this.securityScheme = securityScheme;
	}

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets security scheme.
	 *
	 * @return the security scheme
	 */
	public SecurityScheme getSecurityScheme() {
		return securityScheme;
	}

}
