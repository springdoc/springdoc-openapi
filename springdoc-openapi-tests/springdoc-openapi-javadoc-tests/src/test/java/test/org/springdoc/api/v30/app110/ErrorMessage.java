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

package test.org.springdoc.api.v30.app110;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * The type Error message.
 */
class ErrorMessage {

	/**
	 * The Errors.
	 */
	private List<String> errors;

	/**
	 * Instantiates a new Error message.
	 */
	public ErrorMessage() {
	}

	/**
	 * Instantiates a new Error message.
	 *
	 * @param errors the errors
	 */
	public ErrorMessage(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * Instantiates a new Error message.
	 *
	 * @param error the error
	 */
	public ErrorMessage(String error) {
		this(Collections.singletonList(error));
	}

	/**
	 * Instantiates a new Error message.
	 *
	 * @param errors the errors
	 */
	public ErrorMessage(String... errors) {
		this(Arrays.asList(errors));
	}

	/**
	 * Gets errors.
	 *
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * Sets errors.
	 *
	 * @param errors the errors
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}