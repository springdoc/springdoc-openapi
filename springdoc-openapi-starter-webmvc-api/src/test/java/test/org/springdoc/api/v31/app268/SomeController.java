/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app268;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reproduces issue #3270: a {@code @Pattern} on a {@code @ParameterObject} property named
 * {@code name} must not leak onto the {@code @PathVariable name} of the same (or other)
 * endpoints.
 *
 * @author bnasslahsen
 */
@RestController
public class SomeController {

	@GetMapping("/somethings")
	public String getSomethings() {
		return null;
	}

	@GetMapping("/somethings/{name}/names")
	public String getNames(@PathVariable String name, @ParameterObject SomeFilter filter) {
		return null;
	}

}
