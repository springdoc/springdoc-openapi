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

package test.org.springdoc.api.v30.app83;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {


	@RequestMapping(value = "/{config}",
			method = RequestMethod.PUT,
			consumes = { MediaType.MULTIPART_FORM_DATA_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE }
	)
	public ResponseEntity<?> put(
			@PathVariable("config") final String config,
			@Parameter(name = "configuration", schema = @Schema(name = "configuration", type = "string", format = "binary")) @RequestPart(value = "configuration") final PersonDTO configuration,
			@RequestPart(value = "file") final MultipartFile aFile) {
		return null;
	}

	@RequestMapping(value = "/test",
			method = RequestMethod.PUT,
			consumes = { MediaType.MULTIPART_FORM_DATA_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE }
	)
	@RequestBody(content = @Content(schema = @Schema(requiredProperties = "file", type = "object")
			, schemaProperties = @SchemaProperty(name = "file", schema = @Schema(type = "string", format = "binary"))
	))
	public ResponseEntity<?> put2() {
		return null;
	}
}