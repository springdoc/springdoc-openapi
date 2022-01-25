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

package test.org.springdoc.api.app2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Body.
 */
public class Body {

	/**
	 * Updated name of the pet
	 */
	private String name = null;

	/**
	 * Updated status of the pet
	 */
	private String status = null;

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
	 * Updated name of the pet
	 *
	 * @return name name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Name body.
	 *
	 * @param name the name
	 * @return the body
	 */
	public Body name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Updated status of the pet
	 *
	 * @return status status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status.
	 *
	 * @param status the status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Status body.
	 *
	 * @param status the status
	 * @return the body
	 */
	public Body status(String status) {
		this.status = status;
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
		sb.append("class Body {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
