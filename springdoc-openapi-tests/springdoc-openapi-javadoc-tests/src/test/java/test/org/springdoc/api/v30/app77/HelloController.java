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

package test.org.springdoc.api.v30.app77;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.NotBlank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Persons.
	 *
	 * @param name the name
	 */
	@ApiResponse(content = @Content(schema = @Schema(type = "string")), extensions = @Extension(properties = @ExtensionProperty(name = "x-is-file", value = "true")))
	@GetMapping(value = "/persons")
	public void persons(@Valid @NotBlank String name) {

	}


	/**
	 * Persons 2.
	 *
	 * @param name the name
	 */
	@Operation(responses = @ApiResponse(content = @Content(schema = @Schema(type = "string")), extensions = @Extension(properties = @ExtensionProperty(name = "x-is-file", value = "true"))))
	@GetMapping(value = "/persons2")
	public void persons2(@Valid @NotBlank String name) {

	}

}
