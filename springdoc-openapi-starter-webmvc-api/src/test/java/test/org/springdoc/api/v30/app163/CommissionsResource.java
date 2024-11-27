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

package test.org.springdoc.api.v30.app163;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
public class CommissionsResource {

	@PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE)
	@Operation(description = "updateCommission", summary = "Update a commission")
	@ApiResponse(responseCode = "202", description = "Commission updated", content = @Content(schema = @Schema(implementation = CommissionDto.class), examples = @ExampleObject(name = "202", ref = Examples.PUT_COMMISSION_RESPONSE_BODY_EXAMPLE_KEY)))
	public ResponseEntity<CommissionDto> updateCommission(
			@Parameter(description = "Commission's id", required = true) @PathVariable("id") String commissionId,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "A commission to update", required = true, content = @Content(schema = @Schema(implementation = CommissionDto.class), examples = @ExampleObject(name = "requestExample", ref = Examples.PUT_COMMISSION_REQUEST_BODY_EXAMPLE_KEY))) @RequestBody(required = true) @Valid CommissionDto commission) {
		return accepted().body(commission);
	}

}
