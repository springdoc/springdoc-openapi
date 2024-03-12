/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2023 the original author or authors.
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

package test.org.springdoc.api.v30.app211;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@ApiResponses(value = @ApiResponse(
			responseCode = "200",
			content = {
					@Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schemaProperties = {
									@SchemaProperty(
											name = "items",
											array = @ArraySchema(schema = @Schema(implementation = PagedObject.class))),
									@SchemaProperty(name = "paging", schema = @Schema(implementation = Paging.class))
							}
					)
			}
	))
	@GetMapping
	public String index() {
		return null;
	}

	public class PagedObject {
		private long id;
		private String name;

		public PagedObject(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public class Paging {
		private int page;
		private int total;
		private int lastPage;

		public Paging(int page, int total, int lastPage) {
			this.page = page;
			this.total = total;
			this.lastPage = lastPage;
		}

		public int getPage() {
			return page;
		}

		public int getTotal() {
			return total;
		}

		public int getLastPage() {
			return lastPage;
		}
	}

}
