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

package test.org.springdoc.api.v30.app133;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Gets message from header 1.
	 *
	 * @param header the header
	 * @return the message from header 1
	 */
	@GetMapping(path = "/test1", headers = { "myHeader" })
	public String getMessageFromHeader1(
			@Parameter(name = "myHeader", description = "A header", schema = @Schema(allowableValues = { "foo", "bar" }))
			@RequestHeader("myHeader") String header
	) {
		return "bar " + header;
	}

	/**
	 * Gets message from header 2.
	 *
	 * @param header the header
	 * @return the message from header 2
	 */
	@GetMapping("/test2")
	public String getMessageFromHeader2(
			@Parameter(name = "myHeader", description = "A header", schema = @Schema(type = "integer"))
			@RequestHeader("myHeader") Integer header
	) {
		return "bar " + header;
	}

	/**
	 * Gets message from header 3.
	 *
	 * @param header the header
	 * @return the message from header 3
	 */
	@GetMapping(path = "/test3", headers = { "myHeader" })
	public String getMessageFromHeader3(
			@Parameter(name = "myHeader", description = "A header", schema = @Schema(type = "integer"))
			@RequestHeader("myHeader") Integer header
	) {
		return "bar " + header;
	}
}
