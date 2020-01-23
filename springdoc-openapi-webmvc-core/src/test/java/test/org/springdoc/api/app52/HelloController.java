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

package test.org.springdoc.api.app52;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {

	@PostMapping(value = "/test1/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest1(@PathVariable String username, @RequestPart("test") MyTestDto test,
			@RequestPart("image") MultipartFile imageFile) {
		return null;
	}

	@PostMapping(value = "/test2/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest2(@PathVariable String username, @RequestPart("image") MultipartFile imageFile,
			@RequestPart("test") MyTestDto test) {
		return null;
	}

	@PostMapping(value = "/test3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest3(@RequestPart("test") MyTestDto test,
			@RequestPart("doc") List<MultipartFile> multipartFiles) {
		return null;
	}

	class MyTestDto {
		public String object1;

		public String object2;

		public String object3;
	}
}