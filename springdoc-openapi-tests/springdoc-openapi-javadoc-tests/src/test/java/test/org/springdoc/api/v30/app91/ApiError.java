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

package test.org.springdoc.api.v30.app91;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Api error.
 */
@Schema(
		type = "object",
		name = "ApiError",
		title = "ApiError",
		description = "A consistent response object for sending errors over the wire.")
class ApiError {

	/**
	 * The Status.
	 */
	@Schema(name = "status", description = "The Http Status value", type = "int", nullable = true)
	@JsonProperty("status")
	private int status;

	/**
	 * The Error code.
	 */
	@Schema(
			name = "errorCode",
			description = "An Error Code which can help with identifying issues.",
			type = "string",
			nullable = true)
	@JsonProperty("errorCode")
	private String errorCode;

	/**
	 * The Message.
	 */
	@Schema(name = "message", description = "The Error Message.", type = "string", nullable = false)
	@JsonProperty("message")
	private String message;

	/**
	 * Instantiates a new Api error.
	 *
	 * @param status the status 
	 * @param errorCode the error code 
	 * @param message the message
	 */
	public ApiError(int status, String errorCode, String message) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}
}
