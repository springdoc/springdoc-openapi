/*
 *
 *  * Copyright 2019-2026 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.v31.app212;

import test.org.springdoc.api.v31.app217.PersonDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(value = "/persons1")
	public PersonDTO persons1() {
		return new PersonDTO("John");
	}

	@GetMapping(value = "/persons2")
	public PersonDTO persons2() {
		return new PersonDTO("John");
	}

	@GetMapping(value = "/persons3")
	public PersonDTO persons3() {
		return new PersonDTO("John");
	}

	@PostMapping(value = "/persons3")
	public PersonDTO persons33() {
		return new PersonDTO("John");
	}

}
