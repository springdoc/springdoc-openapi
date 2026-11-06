/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v31.app236;


import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@PostMapping(path = "/testOne", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> testOne(@ParameterObject @Valid ClassOne test) {
		return ResponseEntity.ok("ok");
	}

	@PostMapping(path = "/testTwo", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> testTwo(@RequestBody ClassOne test) {
		return ResponseEntity.ok("ok");
	}
}
