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

package test.org.springdoc.api.v31.app75;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Post my request body 1 string.
	 *
	 * @return the string
	 */
	@PostMapping("/test1/{uuid}")
	@Operation(summary = "Example api that realize an ECHO operation",
			description = "The result of the echo is the input value of the api",
			parameters = { @Parameter(in = ParameterIn.PATH,
					name = "uuid",
					required = true,
					description = "Is the identification of the document",
					schema = @Schema(type = "string",
							example = "uuid")) }


	)
	@ApiResponses(value = {
			@ApiResponse(description = "Successful Operation",
					responseCode = "200",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = PersonDTO.class))),
			@ApiResponse(responseCode = "201",
					description = "other possible response")
	})
	public String postMyRequestBody1() {
		return null;
	}

	/**
	 * Post my request body 2 string.
	 *
	 * @return the string
	 */
	@PostMapping("/test2/{uuid}")
	@Operation(summary = "Example api that realize an ECHO operation",
			description = "The result of the echo is the input value of the api",
			responses = {
					@ApiResponse(description = "Successful Operation",
							responseCode = "200",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = PersonDTO.class))),
					@ApiResponse(responseCode = "201",
							description = "other possible response")
			},
			parameters = { @Parameter(in = ParameterIn.PATH,
					name = "uuid",
					required = true,
					description = "Is the identification of the document",
					schema = @Schema(type = "string",
							example = "uuid")) }


	)
	public String postMyRequestBody2() {
		return null;
	}

	/**
	 * Post my request body 3 string.
	 *
	 * @return the string
	 */
	@PostMapping("/test3/{uuid}")
	@Operation(summary = "Example api that realize an ECHO operation",
			description = "The result of the echo is the input value of the api",
			parameters = { @Parameter(in = ParameterIn.PATH,
					name = "uuid",
					required = true,
					description = "Is the identification of the document",
					schema = @Schema(type = "string",
							example = "uuid")) }


	)
	@ApiResponse(responseCode = "201",
			description = "other possible response")
	public String postMyRequestBody3() {
		return null;
	}

}
