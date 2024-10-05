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

package test.org.springdoc.api.app134;

import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Hello controller.
 */
@SpringBootApplication
@RestController
@Tag(name = "The sample resource")
class HelloController {

	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * The constant VERSION_1.
	 */
	public static final String VERSION_1 = "application/vnd.samples.v1+json";

	/**
	 * The constant VERSION_2.
	 */
	public static final String VERSION_2 = "application/vnd.samples.v2+json";

	/**
	 * The constant HEADER_1.
	 */
	public static final String HEADER_1 = "X-API-VERSION=1";

	/**
	 * The constant HEADER_2.
	 */
	public static final String HEADER_2 = "Accept-version=v2";

	/**
	 * Gets sample v 1.
	 *
	 * @param id the id 
	 * @return the sample v 1
	 */
	@GetMapping(value = "/{id}", produces = VERSION_1, headers = HEADER_1)
	@ResponseStatus(HttpStatus.OK)
	@Operation(operationId = "getSampleV1", deprecated = true, description = "Get the sample by its id."
	)
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SampleV1.class)))
	public SampleV1 getSampleV1(@Parameter(description = "The sample's id", required = true)
	@PathVariable final String id) {
		return new SampleV1(id);
	}

	/**
	 * Gets sample v 2.
	 *
	 * @param id the id 
	 * @return the sample v 2
	 */
	@GetMapping(value = "/{id}", produces = VERSION_2, headers = { HEADER_2, HEADER_1 })
	@ResponseStatus(HttpStatus.OK)
	@Operation(operationId = "getSampleV2", description = "Get the sample by its id. This represents V2.")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SampleV1.class)))
	public SampleV2 getSampleV2(@Parameter(description = "The sample's id", required = true)
	@PathVariable final Long id) {
		return new SampleV2(id);
	}

	/**
	 * Search samples list.
	 *
	 * @param searchRequest the search request 
	 * @return the list
	 */
	@PostMapping(path = "/search", consumes = VERSION_2, produces = VERSION_2)
	@Operation(description = "Searches for sample objects using the given search request.")
	@ApiResponse(responseCode = "200",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = SampleV2.class))))
	public List<SampleV2> searchSamples(@RequestBody final SampleSearchRequest searchRequest) {
		return Collections.singletonList(new SampleV2(searchRequest.getId()));
	}


	/**
	 * The type Sample v 1.
	 */
	private class SampleV1 {

		/**
		 * The Id.
		 */
		private String id;

		/**
		 * Instantiates a new Sample v 1.
		 *
		 * @param id the id
		 */
		public SampleV1(String id) {
			this.id = id;
		}

		/**
		 * Gets id.
		 *
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * Sets id.
		 *
		 * @param id the id
		 */
		public void setId(String id) {
			this.id = id;
		}
	}

	/**
	 * The type Sample v 2.
	 */
	private class SampleV2 {
		/**
		 * The Id.
		 */
		private long id;

		/**
		 * Instantiates a new Sample v 2.
		 *
		 * @param id the id
		 */
		public SampleV2(long id) {
			this.id = id;
		}

		/**
		 * Gets id.
		 *
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * Sets id.
		 *
		 * @param id the id
		 */
		public void setId(long id) {
			this.id = id;
		}
	}

	/**
	 * The type Sample search request.
	 */
	private class SampleSearchRequest {
		/**
		 * The Id.
		 */
		private final long id;

		/**
		 * Instantiates a new Sample search request.
		 *
		 * @param id the id
		 */
		public SampleSearchRequest(long id) {
			this.id = id;
		}

		/**
		 * Gets id.
		 *
		 * @return the id
		 */
		public long getId() {
			return id;
		}
	}
}