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

package test.org.springdoc.api.v31.app244;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HelloController {

	@PostMapping
	@ApiResponse(
			responseCode = "201",
			description = "Created",
			headers = {
					@Header(name = "Location", required = true, schema = @Schema(type = "string"))
			}
	)
	public void uploadMultipartWithBody() {
	}

	@GetMapping
	@io.swagger.v3.oas.annotations.Parameter(
			in = ParameterIn.HEADER,
			name = "x-header",
			examples = {
					@ExampleObject(value = "AAA", name = "First"),
					@ExampleObject(value = "BBB", name = "Second"),
					@ExampleObject(value = "CCC", name = "Third")
			})
	void nope() {
	}

	@PutMapping(value = "/{id}")
	@Parameter(
			in = ParameterIn.PATH,
			name = "id",
			description = "ID of the user to update",
			required = true,
			content = @Content(schema = @Schema(type = "integer")))
	@Operation(summary = "Update a user by passing the entire object")
	public void update(
			@PathVariable("id") @NotNull final String id,
			@Valid @RequestBody @NotNull final String user,
			@Parameter(hidden = true) String spec) {
	}

}