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

package test.org.springdoc.api.app88;

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
import org.springdoc.core.fn.builders.ApiResponseBuilder;
import org.springdoc.core.fn.builders.OperationBuilder;
import org.springdoc.core.fn.builders.ParameterBuilder;
import org.springdoc.core.fn.builders.RequestBodyBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.Constants.OPERATION_ATTRIBUTE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class PostRouter {

	@Bean
	public RouterFunction<ServerResponse> routes(PostHandler postController) {
		return route(GET("/posts").and(queryParam("key", "value")), postController::all)
				.withAttribute(OPERATION_ATTRIBUTE,
						OperationBuilder.builder().operationId("all")
								.parameter(ParameterBuilder.builder().name("key").description("sample description"))
								.parameter(ParameterBuilder.builder().name("test").description("sample desc"))
								.response(ApiResponseBuilder.builder().responseCode("200").implementationArray(Post.class)).build())

				.and(route(POST("/posts"), postController::create)
						.withAttribute(OPERATION_ATTRIBUTE,
								OperationBuilder.builder().operationId("create")
										.requestBody(RequestBodyBuilder.builder().implementation(Post.class))
										.response(ApiResponseBuilder.builder().responseCode("201")).build()))

				.and(route(GET("/posts/{id}"), postController::get)
						.withAttribute(OPERATION_ATTRIBUTE,
								OperationBuilder.builder().operationId("get")
										.parameter(ParameterBuilder.builder().in(ParameterIn.PATH).name("id"))
										.response(ApiResponseBuilder.builder().responseCode("200").implementation(Post.class)).build()))

				.and(route(PUT("/posts/{id}"), postController::update)
						.withAttribute(OPERATION_ATTRIBUTE,
								OperationBuilder.builder().operationId("update")
										.parameter(ParameterBuilder.builder().in(ParameterIn.PATH).name("id"))
										.response(ApiResponseBuilder.builder().responseCode("202").implementation(Post.class)).build()));
	}
}