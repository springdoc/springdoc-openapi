/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app53;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello1", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithNoApiResponse() {
		return null;
	}

	@Operation(description = "Some operation")
	@ApiResponse
	@GetMapping(value = "/hello2", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithEmptyApiResponse() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello3", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	List<String> listWithExplicitResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello4", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	HelloDTO1 getDTOWithExplicitResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello5", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithDefaultResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello6", produces = MediaType.APPLICATION_JSON_VALUE)
	HelloDTO1 getDTOWithDefaultResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello7", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HelloDTO1> getNestedDTOWithDefaultResponseStatus() {
		return null;
	}

	static class HelloDTO1 {
		private final String message;

		public HelloDTO1(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}