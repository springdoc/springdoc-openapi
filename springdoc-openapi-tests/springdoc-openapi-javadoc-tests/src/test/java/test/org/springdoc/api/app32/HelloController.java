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

package test.org.springdoc.api.app32;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Filter post string.
	 *
	 * @param filter the filter 
	 * @return the string
	 */
	@RequestMapping(value = "/filter", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public String filterPost(@RequestBody final MyTestDto filter) {
		return "OK";
	}

	/**
	 * The type My test dto.
	 */
	class MyTestDto {
		/**
		 * The Object 1.
		 */
		public String object1;

		/**
		 * The Object 2.
		 */
		public String object2;

		/**
		 * The Object 3.
		 */
		public String object3;
	}

}
