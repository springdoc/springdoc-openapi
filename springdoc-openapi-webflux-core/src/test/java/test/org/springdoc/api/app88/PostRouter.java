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
						operationBuilder().operationId("all")
								.parameter(parameterBuilder().name("key").description("sample description"))
								.parameter(parameterBuilder().name("test").description("sample desc"))
								.response(responseBuilder().responseCode("200").implementationArray(Post.class)))

				.and(route(POST("/posts"), postController::create)
						.withAttribute(OPERATION_ATTRIBUTE,
								operationBuilder().operationId("create")
										.requestBody(requestBodyBuilder().implementation(Post.class))
										.response(responseBuilder().responseCode("201"))))

				.and(route(GET("/posts/{id}"), postController::get)
						.withAttribute(OPERATION_ATTRIBUTE,
								operationBuilder().operationId("get")
										.parameter(parameterBuilder().in(ParameterIn.PATH).name("id"))
										.response(responseBuilder().responseCode("200").implementation(Post.class))))

				.and(route(PUT("/posts/{id}"), postController::update)
						.withAttribute(OPERATION_ATTRIBUTE,
								operationBuilder().operationId("update")
										.parameter(parameterBuilder().in(ParameterIn.PATH).name("id"))
										.response(responseBuilder().responseCode("202").implementation(Post.class))));
	}
}