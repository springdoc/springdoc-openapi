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

package test.org.springdoc.api.v30.app1;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The type Item controller.
 */
@RestController
@Tag(name = "items")
class ItemController {

	/**
	 * Show items list.
	 *
	 * @param customerID the customer id
	 * @param toto       the toto
	 * @param startDate  the start date
	 * @param filterIds  the filter ids
	 * @return the list
	 */
	@GetMapping("/items")
	public List<ItemDTO> showItems(@RequestParam("cusID") @Size(min = 4, max = 6) final String customerID,
			@Size(min = 4, max = 6) int toto,
			@Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")) @RequestParam(value = "start", required = false) Instant startDate,
			@Parameter(name = "filterIds", in = ParameterIn.QUERY, array = @ArraySchema(schema = @Schema(type = "string")), explode = Explode.FALSE) @RequestParam(required = false) List<String> filterIds) {
		return new ArrayList<ItemDTO>();
	}

	/**
	 * Add item response entity.
	 *
	 * @param itemDTO the item dto
	 * @return the response entity
	 */
	@PostMapping("/items")
	public ResponseEntity<URI> addItem(@Valid @RequestBody final ItemLightDTO itemDTO) {
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(UUID.randomUUID()).toUri();
		return ResponseEntity.created(location).build();
	}

}