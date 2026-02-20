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

package test.org.springdoc.api.v31.app254;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(value = "/class-works/{regularSchema}")
	public RegularSchema dataClass1(@PathVariable RegularSchema regularSchema) {
		return null;
	}

	@GetMapping(value = "/class-works2/{pathSchema}")
	public String dataClass2(@PathVariable PathSchema pathSchema) {
		return null;
	}

	@GetMapping(value = "/class-works3/{itemId}")
	public String dataClass3(@PathVariable ItemId itemId) {
		return null;
	}
}

@Schema(description = "regularSchema")
record RegularSchema(String name, String value) {
}

@Schema(description = "pathSchema", example = "123")
record PathSchema(UUID value) {
}

@Schema(
		type = "uuid",
		description = "Unique item identifier",
		example = "9d9d46e5-d41c-4774-885d-8e9dbc67735c")
record ItemId(UUID value) {

	public static ItemId fromString(String uuid) {
		return new ItemId(UUID.fromString(uuid));
	}
}
