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

package test.org.springdoc.api.v31.app247;

import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
 

@RestController
public class HelloController {

	@PostMapping(value = "/class-works1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String dataClass1(
			@Parameter(
					description = "Metadata as a concrete class",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = MetaData.class)
					)
			)
			@RequestPart("metadata1") MetaData metadata1
	) {
		return "Received metadata: " + metadata1.name() + " = " + metadata1.value();
	}

	@PostMapping(value = "/class-works2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String dataClass2(
			@Parameter(
					description = "Metadata as a concrete class",
					required = true,
					content = @Content(encoding = @Encoding(name = "metadata", contentType = "application/json"),
							schema = @Schema(implementation = MetaData.class)
					)
			)
			@RequestPart("metadata2") MetaData metadata2
	) {
		return "Received metadata: " + metadata2.name() + " = " + metadata2.value();
	}
}

record MetaData(String name, String value, Map<String, String> settings) {
	public MetaData {
		if (settings == null) settings = Map.of(); // default like Kotlin's = emptyMap()
	}
}
