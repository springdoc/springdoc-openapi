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

package test.org.springdoc.api.v30.app61;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


	@Operation(description = "List", parameters = {
			@Parameter(description = "Name", name = "name", in = ParameterIn.QUERY),
			@Parameter(description = "Phone", name = "phone", in = ParameterIn.QUERY),
			@Parameter(description = "createdFrom", name = "createdFrom", in = ParameterIn.QUERY, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
			@Parameter(description = "createdRange", name = "createdRange", in = ParameterIn.QUERY, array = @ArraySchema(schema = @Schema(type = "string", format = "date"), minItems = 2, maxItems = 2))
	})
	@GetMapping(value = "/persons-with-user")
	public String persons(String name, String phone, String createdFrom, String createdRange) {
		return "OK";
	}

}