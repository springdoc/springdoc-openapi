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

package test.org.springdoc.api.v31.app60;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * List 1 list.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the list
	 */
	@GetMapping("/hello1")
	@Operation(summary = "summary1")
	@Parameters({
			@Parameter(name = "page", description = "The page"),
			@Parameter(name = "size", description = "The size")
	})
	public List<?> list1(String page, String size) {
		return null;
	}

	/**
	 * List 2 list.
	 *
	 * @param page the page
	 * @param size the size
	 * @param sort the sort
	 * @return the list
	 */
	@GetMapping("/hello2")
	@Operation(summary = "summary2")
	@QuerySort
	@QueryPaging
	public List<?> list2(String page, String size, String sort) {
		return null;
	}
}
