/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v30.app87;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("cookie")
public class HelloController {

	@PutMapping("/{itemId}")
	@Operation
	public ResponseEntity<Item> putItem(
			@CookieValue(
					name = "cookie"
			) String cookie,
			@PathVariable UUID itemId,
			@RequestBody Item item
	) {
		return ResponseEntity.ok(item);
	}

	/**
	 * List tracker data.
	 *
	 * @return the tracker data
	 */
	@GetMapping(value = "/values/data")
	void list(@RequestHeader(value = "access_token", required = false)
	@Parameter(name = "access_token", in = ParameterIn.HEADER, description = "token in header", schema = @Schema(implementation = String.class))
	String tokenInHeader, @CookieValue(value = "access_token", required = false)
	@Parameter(name = "access_token", in = ParameterIn.COOKIE, description = "token in cookie", schema = @Schema(implementation = String.class))
	String tokenInCookie) {

	}

	@GetMapping("/duplicate_param")
	@Operation(summary = "Duplicate param")
	@Parameter(name = "sample", required = true, in = ParameterIn.HEADER, description = "sample Header")
	@Parameter(name = "sample", required = true, in = ParameterIn.QUERY, description = "sample query")
	public String duplicateParam(@RequestParam String sample, @RequestHeader("sample") String sampleHeader) {
		return "duplicateParam";
	}

	@GetMapping("/duplicate_param2")
	@Operation(summary = "Duplicate param")
	@Parameter(name = "sample", required = true, description = "sample")
	public String duplicateParam2(@RequestParam String sample) {
		return "duplicateParam";
	}

	@GetMapping("/duplicate_param3")
	@Operation(summary = "Duplicate param")
	@Parameter(name = "sample", required = true, description = "sample")
	public String duplicateParam3(@RequestHeader String sample) {
		return "duplicateParam";
	}

	public static class Item {
	}
}