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

package test.org.springdoc.api.app56;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Global exception handler.
 */
@RestControllerAdvice
class GlobalExceptionHandler {
	/**
	 * Handle unhandled error error dto.
	 *
	 * @return the error dto
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiResponse(
			responseCode = "500",
			description = "Internal server error",
			content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ErrorDTO.class)
			)
	)
	ErrorDTO handleUnhandledError() {
		return new ErrorDTO("internal error: ");
	}

	/**
	 * The type Error dto.
	 */
	class ErrorDTO {
		/**
		 * The Error message.
		 */
		private String errorMessage;

		/**
		 * Instantiates a new Error dto.
		 *
		 * @param errorMessage the error message
		 */
		ErrorDTO(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		/**
		 * Gets error message.
		 *
		 * @return the error message
		 */
		public String getErrorMessage() {
			return errorMessage;
		}
	}
}