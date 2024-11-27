/*
 *
 *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.api.app97;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
@RequestMapping("/api")
class HelloController {

	/**
	 * Header v 1 student v 1.
	 *
	 * @return the student v 1
	 */
	@GetMapping(value = "/student/header1", headers = "X-API-VERSION=1")
	public StudentV1 headerV1() {
		return new StudentV1("Bob Charlie");
	}

	/**
	 * Header v 2 student v 2.
	 *
	 * @return the student v 2
	 */
	@GetMapping(value = "/student/header2", headers = "X-API-VERSION=2")
	public StudentV2 headerV2() {
		return new StudentV2("Charlie");
	}

	/**
	 * Header v 3 student v 3.
	 *
	 * @return the student v 3
	 */
	@GetMapping(value = "/student/header3", headers = "X-API-VERSION")
	public StudentV3 headerV3() {
		return new StudentV3("Tom Charlie");
	}
}
