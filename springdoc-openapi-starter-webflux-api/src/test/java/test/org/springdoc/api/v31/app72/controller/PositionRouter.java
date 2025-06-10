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

package test.org.springdoc.api.v31.app72.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import test.org.springdoc.api.v31.app72.entity.Position;
import test.org.springdoc.api.v31.app72.handler.PositionHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class PositionRouter {

	@Bean
	@RouterOperations({ @RouterOperation(path = "/getAllPositions", operation = @Operation(description = "Get all positions", operationId = "findAll", tags = "positions",
			responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Position.class)))))),
			@RouterOperation(path = "/getPosition/{id}", operation = @Operation(description = "Find all", operationId = "findById", tags = "positions", parameters = @Parameter(name = "id", in = ParameterIn.PATH),
					responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Position.class))))),
			@RouterOperation(path = "/createPosition", operation = @Operation(description = "Save position", operationId = "save", tags = "positions", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Position.class))),
					responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Position.class))))),
			@RouterOperation(path = "/deletePosition/{id}", operation = @Operation(description = "Delete By Id", operationId = "deleteBy", tags = "positions", parameters = @Parameter(name = "id", in = ParameterIn.PATH),
					responses = @ApiResponse(responseCode = "200", content = @Content))) })
	public RouterFunction<ServerResponse> positionRoute(PositionHandler handler) {
		return RouterFunctions
				.route(GET("/getAllPositions").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
				.andRoute(GET("/getPosition/{id}").and(accept(MediaType.APPLICATION_STREAM_JSON)), handler::findById)
				.andRoute(POST("/createPosition").and(accept(MediaType.APPLICATION_JSON)), handler::save)
				.andRoute(DELETE("/deletePosition/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);
	}

}
