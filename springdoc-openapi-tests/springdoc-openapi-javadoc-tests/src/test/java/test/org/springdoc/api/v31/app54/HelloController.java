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

package test.org.springdoc.api.v31.app54;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Gets meal party.
	 *
	 * @param mealPartyId the meal party id
	 * @return the meal party
	 */
	@GetMapping(value = "/parties/{id}")
	@JsonView(Views.Public.class)
	@Operation(summary = "Gets meal party details [Meal party admin restricted]")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved the meal party") })
	public MealParty getMealParty(@PathVariable("id") long mealPartyId) {
		return null;
	}

	/**
	 * Save meal party response entity.
	 *
	 * @param p the p
	 * @return the response entity
	 */
	@JsonView(Views.MealPartyAdmin.class)
	@PostMapping(value = "/parties")
	public ResponseEntity<MealParty> saveMealParty(@JsonView(Views.Public.class) @RequestBody MealParty p) {
		return null;
	}

	/**
	 * Save meal new party response entity.
	 *
	 * @param p the p
	 * @return the response entity
	 */
	@JsonView(Views.MealPartyAdmin.class)
	@PostMapping(value = "/new-parties")
	public ResponseEntity<MealParty> saveMealNewParty(@JsonView(Views.Public.class) @org.springframework.web.bind.annotation.RequestBody MealParty p) {
		return null;
	}
}