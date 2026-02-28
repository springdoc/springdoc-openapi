/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v30.app111;

/**
 * The type Problem.
 */
class Problem {

	/**
	 * The Log ref.
	 */
	private String logRef;

	/**
	 * The Message.
	 */
	private String message;

	/**
	 * Instantiates a new Problem.
	 *
	 * @param logRef  the log ref
	 * @param message the message
	 */
	public Problem(String logRef, String message) {
		super();
		this.logRef = logRef;
		this.message = message;
	}

	/**
	 * Instantiates a new Problem.
	 */
	public Problem() {
		super();

	}

	/**
	 * Gets log ref.
	 *
	 * @return the log ref
	 */
	public String getLogRef() {
		return logRef;
	}

	/**
	 * Sets log ref.
	 *
	 * @param logRef the log ref
	 */
	public void setLogRef(String logRef) {
		this.logRef = logRef;
	}

	/**
	 * Gets message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets message.
	 *
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
