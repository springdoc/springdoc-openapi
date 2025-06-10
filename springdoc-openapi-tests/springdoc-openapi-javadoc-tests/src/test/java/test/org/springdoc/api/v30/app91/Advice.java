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

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Advice.
 */
@RestControllerAdvice
class Advice {

	/**
	 * Bad request response entity.
	 *
	 * @param req       the req
	 * @param exception the exception
	 * @return the response entity
	 */
	@ExceptionHandler(TypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ApiResponse(
			responseCode = "400",
			description = "Bad Request",
			content =
			@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ApiError.class),
					examples = {
							@ExampleObject(
									name = "Service-400",
									summary = "400 from the service directly",
									value =
											"{\"status\": 400,"
													+ "\"errorCode\": \"ERROR_001\","
													+ "\"message\": \"An example message...\""
													+ "}")
					}))
	public ResponseEntity<ApiError> badRequest(HttpServletRequest req, Exception exception) {
		ApiError erroObj = new ApiError(400, "A code", "A message");
		return new ResponseEntity<>(erroObj, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Internal server error response entity.
	 *
	 * @param req       the req
	 * @param exception the exception
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiResponse(
			responseCode = "500",
			description = "Internal Server Error",
			content =
			@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ApiError.class),
					examples = {
							@ExampleObject(
									name = "Service-500",
									summary = "500 from the service directly",
									value =
											"{\"status\": 500,"
													+ "\"errorCode\": \"ERROR_002\","
													+ "\"message\": \"Another example message...\""
													+ "}")
					}))
	public ResponseEntity<ApiError> internalServerError(HttpServletRequest req, Exception exception) {
		ApiError erroObj = new ApiError(500, "A  different code", "A  different message");
		return new ResponseEntity<>(erroObj, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
