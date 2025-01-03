/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

package test.org.springdoc.api.v31.app235;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@Validated
@RestController
@RequestMapping("/api")
@Tag(name = "Lorem ipsum")
public class MyDebugController {
	@GetMapping(value = "/debug/{*wildcard}")
	public ResponseEntity<String> getWildcard(String wildcard) {
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}

	// THIS ONE IS MISSING
	@PostMapping(value = "/debug/{*wildcard}")
	public ResponseEntity<String>  postWildcard(String wildcard) {
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/debug/{simple}")
	public ResponseEntity<String> get(String simple) {
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}

	@PostMapping(value = "/debug/{simple}")
	public ResponseEntity<String>  post(String simple) {
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}
}