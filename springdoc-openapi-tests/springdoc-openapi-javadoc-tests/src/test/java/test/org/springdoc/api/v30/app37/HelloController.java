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

package test.org.springdoc.api.v30.app37;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController("/api")
class HelloController {

	/**
	 * Process foo.
	 *
	 * @param a the a
	 * @return the foo
	 */
	@PostMapping(path = "/bar/baz", consumes = "application/x.a+json", produces = MediaType.TEXT_PLAIN_VALUE)
	public Foo process(@RequestBody Foo a) {
		return a;
	}

	/**
	 * Process bar.
	 *
	 * @param b the b
	 * @return the bar
	 */
	@PostMapping(path = "/bar/baz", consumes = "application/x.b+json", produces = MediaType.TEXT_PLAIN_VALUE)
	public Bar process(@RequestBody Bar b) {
		return b;
	}

	/**
	 * Process car.
	 *
	 * @param c the c
	 * @return the car
	 */
	@PostMapping(path = "/bar/baz", consumes = "application/x.c+json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Car process(@RequestBody Car c) {
		return c;
	}


	/**
	 * Pets post response entity.
	 *
	 * @param pet the pet
	 * @return the response entity
	 */
	@PostMapping(value = "/pets1", consumes = "text/plain")
	public ResponseEntity<Void> petsPost1(@Valid @RequestBody String pet) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Pets post response entity.
	 *
	 * @param pet the pet
	 * @return the response entity
	 */
	@PostMapping(value = "/pets2", consumes = "application/json")
	public ResponseEntity<Void> petsPost2(@Valid @RequestBody Pet pet) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
}