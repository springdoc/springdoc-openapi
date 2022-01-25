/*
 *
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.app90.position;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import test.org.springdoc.api.app85.entity.Position;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static test.org.springdoc.api.AbstractSpringDocTest.HANDLER_FUNCTION;

@Configuration
class PositionRouter {

	@Bean
	public RouterFunction<ServerResponse> positionRoute() {
		return route().GET("/getAllPositions", accept(MediaType.APPLICATION_JSON), HANDLER_FUNCTION, ops -> ops
				.operationId("findAll").description("Get all positions").tags(new String[] { "positions" })
				.response(responseBuilder().responseCode("200").implementationArray(Position.class))).build()

				.and(route().GET("/getPosition/{id}", accept(MediaType.APPLICATION_STREAM_JSON), HANDLER_FUNCTION, ops -> ops
						.operationId("findById").description("Find all").tags(new String[] { "positions" })
						.parameter(parameterBuilder().in(ParameterIn.PATH).name("id"))
						.response(responseBuilder().responseCode("200").implementation(Position.class))).build())

				.and(route().POST("/createPosition", accept(MediaType.APPLICATION_JSON), HANDLER_FUNCTION, ops -> ops
						.operationId("save").description("Save position").tags(new String[] { "positions" })
						.requestBody(requestBodyBuilder().implementation(Position.class))
						.response(responseBuilder().responseCode("200").implementation(Position.class))).build())

				.and(route().DELETE("/deletePosition/{id}", accept(MediaType.APPLICATION_JSON), HANDLER_FUNCTION, ops -> ops
						.operationId("deleteBy").description("Delete By Id").tags(new String[] { "positions" })
						.parameter(parameterBuilder().in(ParameterIn.PATH).name("id"))
						.response(responseBuilder().responseCode("200").content(org.springdoc.core.fn.builders.content.Builder.contentBuilder()))).build());
	}
}
