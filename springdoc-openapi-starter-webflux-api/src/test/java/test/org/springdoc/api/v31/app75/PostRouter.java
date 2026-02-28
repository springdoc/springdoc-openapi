/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31.app75;

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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class PostRouter {

	@RouterOperations({ @RouterOperation(path = "/posts", method = RequestMethod.GET, headers = { "x-header1=test1", "x-header2=test2" }, operation = @Operation(operationId = "all",
			parameters = { @Parameter(name = "key", description = "sample description"), @Parameter(name = "test", description = "sample desc") },
			responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class)))))),
			@RouterOperation(path = "/posts", method = RequestMethod.POST, operation = @Operation(operationId = "create",
					requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Post.class))), responses = @ApiResponse(responseCode = "201"))),
			@RouterOperation(path = "/posts/{id}", method = RequestMethod.GET, operation = @Operation(operationId = "get",
					parameters = @Parameter(name = "id", in = ParameterIn.PATH),
					responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class))))),
			@RouterOperation(path = "/posts/{id}", method = RequestMethod.PUT, operation = @Operation(operationId = "update",
					parameters = @Parameter(name = "id", in = ParameterIn.PATH),
					responses = @ApiResponse(responseCode = "202", content = @Content(schema = @Schema(implementation = Post.class))))) })
	@Bean
	public RouterFunction<ServerResponse> routes(PostHandler postController) {
		return route(GET("/posts").and(queryParam("key", "value")), postController::all)
				.andRoute(POST("/posts"), postController::create)
				.andRoute(GET("/posts/{id}"), postController::get)
				.andRoute(PUT("/posts/{id}"), postController::update);
	}
}