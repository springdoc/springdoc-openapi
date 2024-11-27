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

package test.org.springdoc.api.app103;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Post my request body string.
	 *
	 * @param body the body 
	 * @param file the file 
	 * @return the string
	 */
	@PostMapping(value = "/test/103", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
			requestBody = @RequestBody(
					content = @Content(
							encoding = @Encoding(name = "body", contentType = "application/json")
					)
			)
	)
	public String postMyRequestBody(
			@RequestPart("body") ExampleBody body,
			@RequestParam("file") MultipartFile file
	) {
		return null;
	}

}
