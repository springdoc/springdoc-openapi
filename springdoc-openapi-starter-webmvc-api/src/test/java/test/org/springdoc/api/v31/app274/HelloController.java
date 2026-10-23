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

package test.org.springdoc.api.v31.app274;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller with operations mapped to a version other than the default one, and an
 * operation also mapped to a handler method without a version.
 *
 * @author arimu1
 * @author bnasslahsen
 */
@RestController
public class HelloController {

	@GetMapping(value = "/hello", version = "3.0.0")
	@ApiVersion3Parameter
	public String hello() {
		return "hello";
	}

	@GetMapping(value = "/bye", version = "3.0.0")
	public String bye() {
		return "bye";
	}

	@GetMapping("/greeting")
	public String greeting() {
		return "greeting";
	}

	@GetMapping(value = "/greeting", version = "3.0.0")
	public String greetingV3() {
		return "greeting v3";
	}

}
