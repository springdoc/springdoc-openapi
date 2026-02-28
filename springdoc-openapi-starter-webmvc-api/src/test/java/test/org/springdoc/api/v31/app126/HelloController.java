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

package test.org.springdoc.api.v31.app126;

import java.util.ArrayList;
import java.util.Collection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@ApiResponses(value = {
		@ApiResponse(responseCode = "401", ref = SecurityProblemResponsesConfiguration.UNAUTHORIZED_401_NO_TOKEN_RESPONSE_REF),
		@ApiResponse(responseCode = "401", ref = SecurityProblemResponsesConfiguration.UNAUTHORIZED_401_BAD_TOKEN_RESPONSE_REF),
		@ApiResponse(responseCode = "403", ref = SecurityProblemResponsesConfiguration.FORBIDDEN_403_RESPONSE_REF) })
//@ApiResponses(value = {
//    @ApiResponse(responseCode = "401", description = "Invalid authentication.", content = {@Content(schema = @Schema(implementation = Problem.class), mediaType = APPLICATION_PROBLEM_JSON_VALUE)}),
//    @ApiResponse(responseCode = "401", description = "Invalid authentication.",content = {@Content(schema = @Schema(implementation = Problem.class), mediaType = APPLICATION_PROBLEM_JSON_VALUE)}),
//    @ApiResponse(responseCode = "403", description = "Missing authorities.",content = {@Content(schema = @Schema(implementation = Problem.class), mediaType = APPLICATION_PROBLEM_JSON_VALUE)}) })
public class HelloController<T> {

	private static final Collection<String> CURRENCIES = new ArrayList<>();

	static {
		CURRENCIES.add("EUR");
		CURRENCIES.add("USD");
	}

	@GetMapping
	@Operation(description = "Get all currencies", summary = "getAllCurrencies")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "All currencies returned") })
	public ResponseEntity<Collection<String>> getAllCurrencies() {
		return ok(CURRENCIES);
	}

}
