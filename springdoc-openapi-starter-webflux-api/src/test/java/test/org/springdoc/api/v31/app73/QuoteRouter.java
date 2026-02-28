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

package test.org.springdoc.api.v31.app73;

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
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class QuoteRouter {

	@RouterOperations({
			@RouterOperation(path = "/hello", operation = @Operation(operationId = "hello", responses = @ApiResponse(responseCode = "200"))),
			@RouterOperation(path = "/echo", produces = TEXT_PLAIN_VALUE, operation = @Operation(operationId = "echo", requestBody = @RequestBody(content = @Content(schema = @Schema(type = "string"))),
					responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(type = "string"))))),
			@RouterOperation(path = "/echo", produces = APPLICATION_JSON_VALUE, operation = @Operation(operationId = "echo", requestBody = @RequestBody(content = @Content(schema = @Schema(type = "string"))),
					responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(type = "string"))))),
			@RouterOperation(path = "/quotes", produces = APPLICATION_JSON_VALUE, operation = @Operation(operationId = "fetchQuotes", parameters = @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
					responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Quote.class)))))),
			@RouterOperation(path = "/quotes", produces = APPLICATION_OCTET_STREAM_VALUE, operation = @Operation(operationId = "fetchQuotes",
					responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Quote.class))))) })
	@Bean
	public RouterFunction<ServerResponse> route(QuoteHandler quoteHandler) {
		return RouterFunctions
				.route(GET("/hello").and(accept(TEXT_PLAIN)), quoteHandler::hello)
				.andRoute(POST("/echo").and(accept(TEXT_PLAIN).and(contentType(TEXT_PLAIN))), quoteHandler::echo)
				.andRoute(POST("/echo").and(accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON))), quoteHandler::echo)
				.andRoute(GET("/quotes").and(accept(APPLICATION_JSON)), quoteHandler::fetchQuotes)
				.andRoute(GET("/quotes").and(accept(APPLICATION_OCTET_STREAM)), quoteHandler::streamQuotes);
	}
}