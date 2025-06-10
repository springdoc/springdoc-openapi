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

package test.org.springdoc.api.v31.app25;

import java.time.Instant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Check.
	 */
	@GetMapping(value = "/check")
	@ResponseStatus(HttpStatus.OK)
	void check() {
	}

	/**
	 * List.
	 *
	 * @param trackerId the tracker id
	 * @param startDate the start date
	 * @param endDate   the end date
	 */
	@GetMapping(value = "/list/{trackerId}")
	void list(

			@Parameter(name = "trackerId", in = ParameterIn.PATH, required = true, schema = @Schema(type = "string", example = "the-tracker-id")) @PathVariable String trackerId,
			@Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")) @RequestParam(value = "start", required = false) Instant startDate,
			@Parameter(name = "end", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:10:00.000Z")) @RequestParam(value = "end", required = false) Instant endDate) {
	}

	/**
	 * Secondlist.
	 *
	 * @param trackerId the tracker id
	 * @param startDate the start date
	 * @param endDate   the end date
	 */
	@GetMapping(value = "/secondlist/{trackerId}")
	void secondlist(
			@Parameter(name = "trackerId", in = ParameterIn.PATH, required = true, schema = @Schema(type = "string", example = "the-tracker-id")) @PathVariable String trackerId,
			@Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")) @RequestParam(value = "start", required = false) Instant startDate,
			@Parameter(name = "end", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:10:00.000Z")) @RequestParam(value = "end", required = false) Instant endDate) {

	}

	/**
	 * Third list.
	 *
	 * @param trackerId the tracker id
	 * @param start     the start
	 * @param end       the end
	 * @param limit     the limit
	 */
	@Operation(description = "Get last data from a tracker", parameters = {
			@Parameter(name = "trackerId", in = ParameterIn.PATH, required = true, schema = @Schema(type = "string", example = "the-tracker-id")),
			@Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")),
			@Parameter(name = "end", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:10:00.000Z")),
			@Parameter(name = "limit", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "number", required = false, example = "10")) }, responses = {
			@ApiResponse(responseCode = "200") })

	@GetMapping(value = "/values/{trackerId}/data")
	void thirdList(@PathVariable String trackerId, @RequestParam(value = "start", required = false) Instant start,
			@RequestParam(value = "end", required = false) Instant end,
			@RequestParam(value = "limit", required = false) Integer limit) {

	}
}
