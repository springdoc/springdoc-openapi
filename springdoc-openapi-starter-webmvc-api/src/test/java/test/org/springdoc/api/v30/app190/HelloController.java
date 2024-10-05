/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app190;


import org.springdoc.core.annotations.ParameterObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@GetMapping("/nested")
	public ResponseEntity<String> nested(@ParameterObject final SimpleOuterClass filter) {
		return new ResponseEntity<>("{\"Say\": \"Hello\"}", HttpStatus.OK);
	}

	@GetMapping("/nestedTypeErasureGeneric")
	public ResponseEntity<String> nestedTypeErasureGeneric(@ParameterObject final SimpleGeneric<MyData> filter) {
		return new ResponseEntity<>("{\"Say\": \"Hello\"}", HttpStatus.OK);
	}

	@GetMapping("/nestedReifiableGeneric")
	public ResponseEntity<String> nestedReifiableGeneric(@ParameterObject final ConcreteSubclassFromGeneric filter) {
		return new ResponseEntity<>("{\"Say\": \"Hello\"}", HttpStatus.OK);
	}
}
