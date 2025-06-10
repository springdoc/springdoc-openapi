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

package test.org.springdoc.api.v30.app12;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/demo")
@RestController
public class HelloController {

	@Operation(summary = "GetMyData", operationId = "gettt",
			responses = @ApiResponse(responseCode = "204",
					content = @Content(mediaType = "application/vnd.something")))
	@GetMapping(produces = "application/vnd.something")
	public ResponseEntity<Void> getSomethingElse() {
		return ResponseEntity.noContent().build();
	}

	@GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
	public String get() {
		return "some text";
	}

	@GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
	public EntityModel<JsonResponse> getHal() {
		return EntityModel.of(new JsonResponse(),
				WebMvcLinkBuilder.linkTo(HelloController.class).slash("somelink").withSelfRel()
		);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse getJson() {
		return new JsonResponse();
	}

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	@ApiResponse(responseCode = "202",
			content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = JsonResponse.class)))
	public JsonResponse getXML() {
		return new JsonResponse();
	}

	public class JsonResponse {
		@JsonProperty
		private String field;
	}
}
