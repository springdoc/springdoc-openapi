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

package test.org.springdoc.api.v30.app2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Model api response.
 */
public class ModelApiResponse {

	/**
	 * The Code.
	 */
	private Integer code = null;

	/**
	 * The Type.
	 */
	private String type = null;

	/**
	 * The Message.
	 */
	private String message = null;

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 * @param o the o
	 * @return the string
	 */
	private static String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	/**
	 * Get code
	 *
	 * @return code code
	 */
	@JsonProperty("code")
	public Integer getCode() {
		return code;
	}

	/**
	 * Sets code.
	 *
	 * @param code the code
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * Code model api response.
	 *
	 * @param code the code
	 * @return the model api response
	 */
	public ModelApiResponse code(Integer code) {
		this.code = code;
		return this;
	}

	/**
	 * Get type
	 *
	 * @return type type
	 */
	@JsonProperty("type")
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
	 * Type model api response.
	 *
	 * @param type the type
	 * @return the model api response
	 */
	public ModelApiResponse type(String type) {
		this.type = type;
		return this;
	}

	/**
	 * Get message
	 *
	 * @return message message
	 */
	@JsonProperty("message")
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

	/**
	 * Message model api response.
	 *
	 * @param message the message
	 * @return the model api response
	 */
	public ModelApiResponse message(String message) {
		this.message = message;
		return this;
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelApiResponse {\n");

		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    message: ").append(toIndentedString(message)).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
