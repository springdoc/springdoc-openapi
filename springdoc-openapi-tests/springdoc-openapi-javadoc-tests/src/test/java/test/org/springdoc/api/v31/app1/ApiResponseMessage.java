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

package test.org.springdoc.api.v31.app1;

import jakarta.xml.bind.annotation.XmlTransient;

/**
 * The type Api response message.
 */
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-08T09:37:36.546Z[GMT]")
@jakarta.xml.bind.annotation.XmlRootElement
class ApiResponseMessage {
	/**
	 * The constant ERROR.
	 */
	public static final int ERROR = 1;

	/**
	 * The constant WARNING.
	 */
	public static final int WARNING = 2;

	/**
	 * The constant INFO.
	 */
	public static final int INFO = 3;

	/**
	 * The constant OK.
	 */
	public static final int OK = 4;

	/**
	 * The constant TOO_BUSY.
	 */
	public static final int TOO_BUSY = 5;

	/**
	 * The Code.
	 */
	int code;

	/**
	 * The Type.
	 */
	String type;

	/**
	 * The Message.
	 */
	String message;

	/**
	 * Instantiates a new Api response message.
	 */
	public ApiResponseMessage() {
	}

	/**
	 * Instantiates a new Api response message.
	 *
	 * @param code    the code
	 * @param message the message
	 */
	public ApiResponseMessage(int code, String message) {
		this.code = code;
		switch (code) {
			case ERROR:
				setType("error");
				break;
			case WARNING:
				setType("warning");
				break;
			case INFO:
				setType("info");
				break;
			case OK:
				setType("ok");
				break;
			case TOO_BUSY:
				setType("too busy");
				break;
			default:
				setType("unknown");
				break;
		}
		this.message = message;
	}

	/**
	 * Gets code.
	 *
	 * @return the code
	 */
	@XmlTransient
	public int getCode() {
		return code;
	}

	/**
	 * Sets code.
	 *
	 * @param code the code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets type.
	 *
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
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
