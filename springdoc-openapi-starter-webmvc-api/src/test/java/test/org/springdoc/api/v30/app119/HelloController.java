/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

package test.org.springdoc.api.v30.app119;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {

	@Operation(summary = "Multiple files and JSON payloads as multi part request")
	@PostMapping(
			value = "multi",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public String multiFilesInMultiPart(
			@RequestPart("params")
			@RequestBody(content = @Content(encoding = @Encoding(name = "params", contentType = MediaType.APPLICATION_JSON_VALUE)))
			@Parameter(
					description = "This is the configuration",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) final JsonRequest jsonRequest,
			@RequestPart(value = "file1", required = false) @Parameter(description = "This is file1") final MultipartFile file1,
			@RequestPart(value = "file2", required = false) @Parameter(description = "This is file2") final MultipartFile file2) {
		return "Hello World " + jsonRequest.getName();
	}
}