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

package test.org.springdoc.api.v31.app53;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * List with no api response list.
	 *
	 * @return the list
	 */
	@Operation(description = "Some operation")
	@GetMapping(value = "/hello1", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithNoApiResponse() {
		return null;
	}

	/**
	 * List with empty api response list.
	 *
	 * @return the list
	 */
	@Operation(description = "Some operation")
	@ApiResponse
	@GetMapping(value = "/hello2", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithEmptyApiResponse() {
		return null;
	}

	/**
	 * List with explicit response status list.
	 *
	 * @return the list
	 */
	@Operation(description = "Some operation")
	@GetMapping(value = "/hello3", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	List<String> listWithExplicitResponseStatus() {
		return null;
	}

	/**
	 * Gets dto with explicit response status.
	 *
	 * @return the dto with explicit response status
	 */
	@Operation(description = "Some operation")
	@GetMapping(value = "/hello4", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	HelloDTO1 getDTOWithExplicitResponseStatus() {
		return null;
	}

	/**
	 * List with default response status list.
	 *
	 * @return the list
	 */
	@Operation(description = "Some operation")
	@GetMapping(value = "/hello5", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithDefaultResponseStatus() {
		return null;
	}

	/**
	 * Gets dto with default response status.
	 *
	 * @return the dto with default response status
	 */
	@Operation(description = "Some operation")
	@GetMapping(value = "/hello6", produces = MediaType.APPLICATION_JSON_VALUE)
	HelloDTO1 getDTOWithDefaultResponseStatus() {
		return null;
	}

	/**
	 * Gets nested dto with default response status.
	 *
	 * @return the nested dto with default response status
	 */
	@Operation(description = "Some operation")
	@GetMapping(value = "/hello7", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HelloDTO1> getNestedDTOWithDefaultResponseStatus() {
		return null;
	}

	/**
	 * The type Hello dto 1.
	 */
	static class HelloDTO1 {
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
}