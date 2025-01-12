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

package test.org.springdoc.api.v30.app161;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {


	/**
	 * Add response entity.
	 *
	 * @param content the content 
	 * @return the response entity 
	 * @throws Exception the exception
	 */
	@Operation(summary = "add")
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> add(@Parameter(description = "content") @RequestPart(value = "content") String content) throws Exception {
		return null;
	}


	/**
	 * Add 2 response entity.
	 *
	 * @param content the content 
	 * @param type the type 
	 * @return the response entity
	 */
	@Operation(summary = "add2")
	@PostMapping(value = "/add2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> add2(
			@Parameter(description = "content") @RequestPart(value = "content") String content,
			@RequestPart(value = "type") String type
	) {
		return null;
	}

	/**
	 * Test.
	 *
	 * @param strValue the str value 
	 * @param intValue the int value
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "test")
	public void test(@RequestPart("strValue") String strValue,
			@RequestPart("intValue") Integer intValue) {
	}
}
