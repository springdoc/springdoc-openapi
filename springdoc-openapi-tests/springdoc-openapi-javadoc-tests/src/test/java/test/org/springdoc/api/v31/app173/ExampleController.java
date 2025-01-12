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

package test.org.springdoc.api.v31.app173;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

/**
 * The Example Controller
 */
@RestController
class ExampleController {

	@PostMapping("/example")
	@Operation(summary = "insert example", description = "Allows to insert an example")
	public ResponseEntity<UUID> postExample(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "${example.description}") @RequestBody Example example) {
		return new ResponseEntity<>(UUID.randomUUID(), OK);
	}

	@PutMapping("/example")
	@Operation(summary = "update example", description = "Allows to update an example")
	public ResponseEntity<UUID> putExample(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "${example2.description:Default description for example}") @RequestBody Example example) {
		return new ResponseEntity<>(UUID.randomUUID(), OK);
	}

	@PatchMapping("/example")
	@Operation(summary = "patch example", description = "Allows to patch an example")
	public ResponseEntity<UUID> patchExample(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Description without the use of variables") @RequestBody Example example) {
		return new ResponseEntity<>(UUID.randomUUID(), OK);
	}
}
