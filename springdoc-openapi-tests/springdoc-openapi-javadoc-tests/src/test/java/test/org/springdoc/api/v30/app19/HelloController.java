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

package test.org.springdoc.api.v30.app19;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Persons string.
	 *
	 * @param name the name
	 * @return the string
	 */
	@PostMapping(value = "/persons")
	public String persons(@RequestBody(description = "requestBody description as parameter") String name) {
		return "OK";
	}

	/**
	 * Persons 2 string.
	 *
	 * @param name the name
	 * @return the string
	 */
	@RequestBody(description = "requestBody description outside")
	@PostMapping(value = "/persons2")
	public String persons2(String name) {
		return "OK";
	}

	/**
	 * Persons 3 string.
	 *
	 * @param name the name
	 * @return the string
	 */
	@Operation(requestBody = @RequestBody(description = "requestBody inside operation annotation"))
	@PostMapping(value = "/persons3")
	public String persons3(@NotBlank String name) {
		return "OK";
	}

}
