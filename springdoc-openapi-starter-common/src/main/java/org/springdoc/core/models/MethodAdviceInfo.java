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
package org.springdoc.core.models;

import java.lang.reflect.Method;
import java.util.Set;

import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * The type Method advice info.
 *
 * @author bnasslahsen
 */
public class MethodAdviceInfo {

	/**
	 * The Method.
	 */
	private final Method method;

	/**
	 * The Exceptions.
	 */
	private Set<Class<?>> exceptions;

	/**
	 * The Api responses.
	 */
	private ApiResponses apiResponses;

	/**
	 * Instantiates a new Method advice info.
	 *
	 * @param method the method
	 */
	public MethodAdviceInfo(Method method) {
		this.method = method;
	}

	/**
	 * Gets method.
	 *
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Gets exceptions.
	 *
	 * @return the exceptions
	 */
	public Set<Class<?>> getExceptions() {
		return exceptions;
	}

	/**
	 * Sets exceptions.
	 *
	 * @param exceptions the exceptions
	 */
	public void setExceptions(Set<Class<?>> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * Gets api responses.
	 *
	 * @return the api responses
	 */
	public ApiResponses getApiResponses() {
		return apiResponses;
	}

	/**
	 * Sets api responses.
	 *
	 * @param apiResponses the api responses
	 */
	public void setApiResponses(ApiResponses apiResponses) {
		this.apiResponses = apiResponses;
	}
}