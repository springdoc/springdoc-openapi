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

package test.org.springdoc.api.v30.app78;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Gets person 1.
	 *
	 * @param str the str
	 * @return the person 1
	 */
	@RequestMapping(value = "/person1", method = RequestMethod.GET)
	private CompletionStage<ResponseEntity<PersonDTO>> getPerson1(String str) {
		return null;
	}

	/**
	 * Gets person 2.
	 *
	 * @param str the str
	 * @return the person 2
	 */
	@RequestMapping(value = "/person2", method = RequestMethod.GET)
	private CompletableFuture<PersonDTO> getPerson2(String str) {
		return null;
	}

}