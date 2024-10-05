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

package test.org.springdoc.api.app18;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@GetMapping(value = "/persons")
	public String persons(@NotBlank String name) {
		return "OK";
	}

	/**
	 * Persons 2 string.
	 *
	 * @param name the name 
	 * @return the string
	 */
	@GetMapping(value = "/persons2")
	public String persons2(@NotBlank @Parameter(description = "persons name") String name) {
		return "OK";
	}

	/**
	 * Persons 3 string.
	 *
	 * @param name the name 
	 * @return the string
	 */
	@GetMapping(value = "/persons3")
	public String persons3(@NotBlank @Parameter(description = "persons name") @RequestParam String name) {
		return "OK";
	}

	/**
	 * Persons 4 string.
	 *
	 * @param age the age 
	 * @return the string
	 */
	@GetMapping(value = "/persons4")
	public String persons4(@PositiveOrZero int age) {
		return "OK";
	}

	/**
	 * Persons 5 string.
	 *
	 * @param age the age 
	 * @return the string
	 */
	@GetMapping(value = "/persons5")
	public String persons5(@NegativeOrZero int age) {
		return "OK";
	}

	/**
	 * Persons 6 string.
	 *
	 * @param name the name 
	 * @return the string
	 */
	@GetMapping(value = "/persons6")
	public String persons6(@NotEmpty @Parameter(description = "persons name") String name) {
		return "OK";
	}

}
