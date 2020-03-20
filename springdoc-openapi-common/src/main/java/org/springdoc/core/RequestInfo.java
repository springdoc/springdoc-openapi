/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

public class RequestInfo {

	private final String value;
	private final boolean required;
	private final String defaultValue;
	private final String paramType;

	public RequestInfo(String paramType, String value, boolean required, String defaultValue) {
		this.value = value;
		this.required = required;
		this.defaultValue = defaultValue;
		this.paramType = paramType;
	}

	public String value() {
		return value;
	}

	public boolean required() {
		return required;
	}

	public String defaultValue() {
		return defaultValue;
	}

	public String type() {
		return paramType;
	}

}
