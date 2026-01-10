/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app139;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(produces = MediaType.TEXT_PLAIN_VALUE, path = "/test1")
	@Parameter(name = "parameter", in = ParameterIn.QUERY,
			schema = @Schema(type = "string", defaultValue = "${test.parameter-default-value}"))
	@Parameter(name = "parameter-boolean", in = ParameterIn.QUERY,
			schema = @Schema(type = "boolean", defaultValue = "${test.parameter-boolean-default-value}"))
	public String echo1(@RequestParam(name = "${test.name}", defaultValue = "${test.default-value}") String text) {
		return text;
	}

	@GetMapping(produces = MediaType.TEXT_PLAIN_VALUE, path = "/test2")
	public String echo2(@RequestParam(value = "${test.value}", defaultValue = "${test.default-value}") String text) {
		return text;
	}

}
