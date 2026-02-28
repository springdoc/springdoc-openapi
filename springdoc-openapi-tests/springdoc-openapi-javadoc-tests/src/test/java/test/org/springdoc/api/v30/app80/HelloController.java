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

package test.org.springdoc.api.v30.app80;

import java.net.URISyntaxException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
@RequestMapping("/api")
class HelloController {


	/**
	 * Testpost 1 response entity.
	 *
	 * @param dto the dto
	 * @return the response entity
	 * @throws URISyntaxException the uri syntax exception
	 */
	@RequestMapping(value = "/testpost1", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestObject> testpost1(@RequestBody TestObject dto) throws URISyntaxException {
		return ResponseEntity.ok(dto);
	}

	/**
	 * Testpost 2 response entity.
	 *
	 * @param dto the dto
	 * @return the response entity
	 * @throws URISyntaxException the uri syntax exception
	 */
	@RequestMapping(value = "/testpost2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestObject> testpost2(@RequestBody TestObject dto) throws URISyntaxException {
		return ResponseEntity.ok(dto);
	}

	/**
	 * Hello response entity.
	 *
	 * @return the response entity
	 * @throws URISyntaxException the uri syntax exception
	 */
	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> hello() throws URISyntaxException {
		return ResponseEntity.ok("Hello World");
	}

}
