/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package test.org.springdoc.api.app86;

import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.Constants.OPERATION_ATTRIBUTE;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class QuoteRouter {

	@Bean
	public RouterFunction<ServerResponse> myroute(QuoteHandler quoteHandler) {
		return route(GET("/hello").and(accept(TEXT_PLAIN)), quoteHandler::hello)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().operationId("hello").response(responseBuilder().responseCode("200")))

				.and(route(POST("/echo").and(accept(TEXT_PLAIN).and(contentType(TEXT_PLAIN))), quoteHandler::echo)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().operationId("echo")
								.requestBody(requestBodyBuilder().implementation(String.class))
								.response(responseBuilder().responseCode("200").implementation(String.class))))

				.and(route(POST("/echo").and(accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON))), quoteHandler::echo)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().operationId("echo")
								.requestBody(requestBodyBuilder().implementation(String.class))
								.response(responseBuilder().responseCode("200").implementation(String.class)))
				)

				.and(route(GET("/quotes").and(accept(APPLICATION_JSON)), quoteHandler::fetchQuotes)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().operationId("fetchQuotes")
								.parameter(parameterBuilder().in(ParameterIn.QUERY).name("size").implementation(String.class))
								.response(responseBuilder().responseCode("200").implementationArray(Quote.class))))

				.and(route(GET("/quotes").and(accept(APPLICATION_STREAM_JSON)), quoteHandler::streamQuotes)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().operationId("fetchQuotes")
								.response(responseBuilder().responseCode("200").implementation(Quote.class))));
	}

}