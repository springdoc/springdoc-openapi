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

package test.org.springdoc.api.app41;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController("/api")
class HelloController {

	/**
	 * Gets file.
	 *
	 * @param path the path 
	 * @return the file
	 */
	@Operation(description = "Download file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "File resource", content = @Content(schema = @Schema(implementation = java.io.File.class))),
			@ApiResponse(responseCode = "400", description = "Wrong request", content = @Content(schema = @Schema(implementation = Error.class))),
			@ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = Error.class))) })
	@GetMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<java.io.File> getFile(
			@NotNull @Parameter(description = "File path", required = true) @Valid @RequestParam(value = "path") String path) {
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

}