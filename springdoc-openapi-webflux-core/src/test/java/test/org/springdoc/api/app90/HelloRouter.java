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

package test.org.springdoc.api.app90;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static test.org.springdoc.api.AbstractSpringDocTest.HANDLER_FUNCTION;

@Configuration
class HelloRouter {

	@Bean
	RouterFunction<?> routes() {
		return route().GET("/foo", HANDLER_FUNCTION, ops -> ops
				.operationId("hello")
				.parameter(parameterBuilder().name("key1").description("My key1 description"))
				.parameter(parameterBuilder().name("key2").description("My key2 description"))
				.response(responseBuilder().responseCode("200").description("This is normal response description"))
				.response(responseBuilder().responseCode("404").description("This is another response description"))
		).build();
	}

}