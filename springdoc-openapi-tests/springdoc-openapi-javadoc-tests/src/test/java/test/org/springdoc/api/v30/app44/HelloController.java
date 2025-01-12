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

package test.org.springdoc.api.v30.app44;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController("/api")
class HelloController {

	/**
	 * Hello response entity.
	 *
	 * @param request the request 
	 * @return the response entity
	 */
	@PostMapping(value = "/helloworld", produces = "application/json", consumes = "application/vnd.v1+json")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = HelloDTO1.class))),
			@ApiResponse(responseCode = "400", description = "Bad name", content = @Content(schema = @Schema(implementation = ErrorDTO.class))) })
	public ResponseEntity<?> hello(@RequestBody RequestV1 request) {
		final String name = request.getNameV1();
		if ("error".equalsIgnoreCase(name)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("invalid name: " + name));
		}
		return ResponseEntity.ok(new HelloDTO1("Greetings from Spring Boot v1! " + name));
	}

	/**
	 * Hello response entity.
	 *
	 * @param request the request 
	 * @return the response entity
	 */
	@PostMapping(value = "/helloworld", produces = "application/json", consumes = "application/vnd.v2+json")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = HelloDTO2.class))),
			@ApiResponse(responseCode = "400", description = "Bad name", content = @Content(schema = @Schema(implementation = ErrorDTO.class))) })
	public ResponseEntity<?> hello(@RequestBody RequestV2 request) {
		final String name = request.getNameV2();
		if ("error".equalsIgnoreCase(name)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("invalid name: " + name));
		}
		return ResponseEntity.ok(new HelloDTO2("Greetings from Spring Boot v2! " + name));
	}

	/**
	 * The type Request v 1.
	 */
	static class RequestV1 {
		/**
		 * The Name v 1.
		 */
		private String nameV1;

		/**
		 * Instantiates a new Request v 1.
		 */
		public RequestV1() {
		}

		/**
		 * Gets name v 1.
		 *
		 * @return the name v 1
		 */
		public String getNameV1() {
			return nameV1;
		}

		/**
		 * Sets name v 1.
		 *
		 * @param nameV1 the name v 1
		 */
		public void setNameV1(String nameV1) {
			this.nameV1 = nameV1;
		}
	}

	/**
	 * The type Request v 2.
	 */
	static class RequestV2 {
		/**
		 * The Name v 2.
		 */
		private String nameV2;

		/**
		 * Instantiates a new Request v 2.
		 */
		public RequestV2() {
		}

		/**
		 * Gets name v 2.
		 *
		 * @return the name v 2
		 */
		public String getNameV2() {
			return nameV2;
		}

		/**
		 * Sets name v 2.
		 *
		 * @param nameV2 the name v 2
		 */
		public void setNameV2(String nameV2) {
			this.nameV2 = nameV2;
		}
	}

	/**
	 * The type Hello dto 1.
	 */
	class HelloDTO1 {
		/**
		 * The Message.
		 */
		private String message;

		/**
		 * Instantiates a new Hello dto 1.
		 *
		 * @param message the message
		 */
		public HelloDTO1(String message) {
			this.message = message;
		}

		/**
		 * Gets message.
		 *
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
	}

	/**
	 * The type Hello dto 2.
	 */
	class HelloDTO2 {
		/**
		 * The Message.
		 */
		private String message;

		/**
		 * Instantiates a new Hello dto 2.
		 *
		 * @param message the message
		 */
		public HelloDTO2(String message) {
			this.message = message;
		}

		/**
		 * Gets message.
		 *
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
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
		public ErrorDTO(String errorMessage) {
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