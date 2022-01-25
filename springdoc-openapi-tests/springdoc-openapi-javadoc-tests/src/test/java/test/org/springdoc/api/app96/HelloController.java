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

package test.org.springdoc.api.app96;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
public class HelloController {


	/**
	 * Test 1 string.
	 *
	 * @param test the test 
	 * @return the string
	 */
	@PostMapping("/api1")
	String test1(@RequestBody @Min(2) int test) {
		return null;
	}

	/**
	 * Test 2 string.
	 *
	 * @param test the test 
	 * @return the string
	 */
	@PostMapping("/api2")
	String test2(@RequestBody String test) {
		return null;
	}

	/**
	 * Test 3 string.
	 *
	 * @param test the test 
	 * @return the string
	 */
	@PostMapping("/api3")
	String test3(@NotNull String test) {
		return null;
	}

}
