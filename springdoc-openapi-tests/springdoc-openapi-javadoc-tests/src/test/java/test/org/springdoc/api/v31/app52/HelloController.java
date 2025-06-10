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

package test.org.springdoc.api.v31.app52;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Create test 1 string.
	 *
	 * @param username  the username
	 * @param test      the test
	 * @param imageFile the image file
	 * @return the string
	 */
	@PostMapping(value = "/test1/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest1(@PathVariable String username, @RequestPart("test") MyTestDto test,
			@RequestPart("image") MultipartFile imageFile) {
		return null;
	}

	/**
	 * Create test 2 string.
	 *
	 * @param username  the username
	 * @param imageFile the image file
	 * @param test      the test
	 * @return the string
	 */
	@PostMapping(value = "/test2/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest2(@PathVariable String username, @RequestPart("image") MultipartFile imageFile,
			@RequestPart("test") MyTestDto test) {
		return null;
	}

	/**
	 * Create test 3 string.
	 *
	 * @param test           the test
	 * @param multipartFiles the multipart files
	 * @return the string
	 */
	@PostMapping(value = "/test3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest3(@RequestPart("test") MyTestDto test,
			@RequestPart("doc") List<MultipartFile> multipartFiles) {
		return null;
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