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

package test.org.springdoc.api.v30.app156;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {
	/**
	 * Hello string.
	 *
	 * @param user the user
	 * @return the string
	 */
	@GetMapping("/hello")
	@Parameter(name = "someEnums", in = QUERY, description = "SomeEum decs",
			array = @ArraySchema(schema = @Schema(implementation = SomeEnum.class)))
	@Parameter(name = "textSet", in = QUERY, description = "First decs",
			array = @ArraySchema(schema = @Schema(implementation = String.class)))
	@Parameter(name = "someText", in = QUERY, description = "Second decs",
			schema = @Schema(type = "string"))
	public String hello(@Parameter(hidden = true) User user) {
		String forReturn = "Hello ";
		StringBuilder stringBuilder = new StringBuilder(forReturn);

		if (user.getSomeEnums() != null) {
			for (SomeEnum some : user.getSomeEnums()) {
				stringBuilder.append(some);
				stringBuilder.append(" ");
			}
		}

		if (user.getSomeText() != null) {
			for (String text : user.getTextSet()) {
				stringBuilder.append(text);
				stringBuilder.append(" ");
			}
		}

		if (user.getSomeText() != null) {
			stringBuilder.append(user.getSomeText());
		}

		return stringBuilder.toString();
	}
}
