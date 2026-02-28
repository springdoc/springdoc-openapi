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

package test.org.springdoc.api.v31.app51;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Test 1 string.
	 *
	 * @return the string
	 */
	@Operation(parameters = {
			@Parameter(in = ParameterIn.HEADER, name = "test_header", required = true, schema = @Schema(type = "string", example = "rherherherherh")) })
	@GetMapping("/test1")
	public String test1() {
		return "test";
	}

	/**
	 * Test 2 string.
	 *
	 * @param param1 the param 1
	 * @return the string
	 */
	@Operation(parameters = {
			@Parameter(in = ParameterIn.HEADER, name = "test_header", required = true, schema = @Schema(type = "string", example = "rherherherherh")) })
	@GetMapping("/test2")
	public String test2(@RequestParam(name = "param1") String param1) {
		return "test";
	}

	/**
	 * Test 3 string.
	 *
	 * @param param1 the param 1
	 * @return the string
	 */
	@Operation(parameters = {
			@Parameter(in = ParameterIn.HEADER, name = "test_header", required = true, schema = @Schema(type = "string", example = "rherherherherh")),
			@Parameter(description = "desc1", in = ParameterIn.QUERY, name = "param1", required = true, schema = @Schema(type = "string", example = "something")) })
	@GetMapping("/test3")
	public String test3(
			@RequestParam(name = "param1") @Parameter(description = "desc2", in = ParameterIn.QUERY) String param1) {
		return "test";
	}

	/**
	 * Get string.
	 *
	 * @param path   the path
	 * @param params the params
	 * @return the string
	 */
	@GetMapping("/test/{path}")
	public String get(
			@PathVariable String path,
			@RequestParam(required = false) Map<String, String> params) {
		return null;
	}

	/**
	 * Hello response entity.
	 *
	 * @param map the map
	 * @return the response entity
	 */
	@PostMapping
	public ResponseEntity<HashMap<String, Object>> hello(@RequestBody HashMap<String, Object> map) {
		return ResponseEntity.ok(map);
	}
}