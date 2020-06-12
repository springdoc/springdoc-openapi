/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core;

/**
 * The type Request info.
 * @author bnasslahsen
 */
public class RequestInfo {

	/**
	 * The Value.
	 */
	private final String value;

	/**
	 * The Required.
	 */
	private final boolean required;

	/**
	 * The Default value.
	 */
	private final String defaultValue;

	/**
	 * The Param type.
	 */
	private final String paramType;

	/**
	 * Instantiates a new Request info.
	 *
	 * @param paramType the param type
	 * @param value the value
	 * @param required the required
	 * @param defaultValue the default value
	 */
	public RequestInfo(String paramType, String value, boolean required, String defaultValue) {
		this.value = value;
		this.required = required;
		this.defaultValue = defaultValue;
		this.paramType = paramType;
	}

	/**
	 * Value string.
	 *
	 * @return the string
	 */
	public String value() {
		return value;
	}

	/**
	 * Required boolean.
	 *
	 * @return the boolean
	 */
	public boolean required() {
		return required;
	}

	/**
	 * Default value string.
	 *
	 * @return the string
	 */
	public String defaultValue() {
		return defaultValue;
	}

	/**
	 * Type string.
	 *
	 * @return the string
	 */
	public String type() {
		return paramType;
	}

}
