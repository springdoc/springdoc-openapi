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

package test.org.springdoc.api.v31.app1;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import tools.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-08T09:37:36.546Z[GMT]")
@RestController
public class InventoryApiController implements InventoryApi {


	@SuppressWarnings("unused")
	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public InventoryApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<Void> addInventory(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Inventory item to add") @Valid @RequestBody InventoryItem body) {
		@SuppressWarnings("unused")
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<List<InventoryItem>> searchInventory(
			@Parameter(description = "pass an optional search string for looking up inventory") @Valid @RequestParam(value = "searchString", required = false) String searchString,
			@Min(0) @Parameter(description = "number of records to skip for pagination") @Valid @RequestParam(value = "skip", required = true) Integer skip,
			@Min(0) @Max(50) @Parameter(description = "maximum number of records to return") @Valid @RequestParam(value = "limit", required = true) Integer limit) {
		@SuppressWarnings("unused")
		String accept = request.getHeader("Accept");
		return new ResponseEntity<List<InventoryItem>>(HttpStatus.NOT_IMPLEMENTED);
	}

	public String getme(String language) {
		return language;
	}

}
