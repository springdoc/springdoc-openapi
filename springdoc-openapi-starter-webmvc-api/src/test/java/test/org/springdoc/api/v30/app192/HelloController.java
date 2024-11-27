/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app192;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@Operation(description = "Adds 200 as api response, because there are nothing defined to get another response")
	@RequestBody(description = "test value", required = true, content = @Content(schema = @Schema(implementation = String.class)))
	@PostMapping(value = "/postWithoutAnyResponse", produces = MediaType.APPLICATION_JSON_VALUE)
	public void postWithoutAnyResponse(String test) {
	}

	@Operation(description = "Adds 201 as api response, because it defined by @ResponseStatus")
	@PostMapping(value = "/postWithResponseStatusOnly", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<String> postWithResponseStatusOnly() {
		return ResponseEntity.ok("hello");
	}

	@Operation(description = "Adds 200 as additional api response, because it defined by @ResponseStatus",
			responses = {
					@ApiResponse(responseCode = "422", description = "Test")
			})
	@ApiResponses({
			@ApiResponse(responseCode = "409", description = "Test 2")
	})
	@GetMapping(value = "/withResponseStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> withResponseStatus() {
		return ResponseEntity.ok("hello");
	}

	@Operation(description = "Adds 200 as api response, because it defined by @ResponseStatus")
	@GetMapping(value = "/withResponseStatusOnly", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> withResponseStatusOnly() {
		return ResponseEntity.ok("hello");
	}

	@Operation(description = "Doesn't creates the default 200 Response, because there are explicit declared api responses." +
			"This test ensures that the current default handling is not changed, because otherwise very many tests will fail.",
			responses = {
					@ApiResponse(responseCode = "422", description = "Test")
			})
	@ApiResponses({
			@ApiResponse(responseCode = "409", description = "Test 2") })
	@GetMapping(value = "/withoutResponseStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> withoutResponseStatus() {
		return ResponseEntity.ok("hello");
	}


	@Operation(description = "Results in the default handling like before")
	@GetMapping(value = "/withoutAnyResponseInformation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> withoutAnyResponseInformation() {
		return ResponseEntity.ok("hello");
	}

	@Operation(description = "Overwrites the 200 @ResponseStatus-Information by the explicit declared @ApiResponse",
			responses = {
					@ApiResponse(responseCode = "200", description = "Test2", content = @Content),
					@ApiResponse(responseCode = "422", description = "Test")
			})
	@ApiResponses({
			@ApiResponse(responseCode = "409", description = "Test 2")
	})
	@GetMapping(value = "/overwrite200InOperation", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> overwrite200InOperation() {
		return ResponseEntity.ok("hello");
	}

	@Operation(description = "Overwrites the 200 @ResponseStatus-Information by the explicit declared @ApiResponse",
			responses = {
					@ApiResponse(responseCode = "422", description = "Test")
			})
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Test2", content = @Content),
			@ApiResponse(responseCode = "409", description = "Test 2")
	})
	@GetMapping(value = "/overwrite200InDoc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> overwrite200InDoc() {
		return ResponseEntity.ok("hello");
	}
}
