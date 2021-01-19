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

package test.org.springdoc.api.app150;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springdoc.core.fn.builders.operation.Builder;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static test.org.springdoc.api.AbstractSpringDocTest.HANDLER_FUNCTION;

@Configuration
public class HelloRouter {

	@Bean
	RouterFunction<?> routeSample() {
		Supplier<RouterFunction<ServerResponse>> routerFunctionSupplier =
				() -> SpringdocRouteBuilder.route()
						.GET("toto", HANDLER_FUNCTION, builder -> builder.operationId("get-user-groups"))

						.POST("/titi", HANDLER_FUNCTION, builder -> builder.operationId("create-user-group-special")).build();

		Consumer<Builder> operationsConsumer = builder ->  { };

			return 	RouterFunctions.nest(RequestPredicates.path("/users"), nest(path("/test"), nest(path("/greeter"),
					SpringdocRouteBuilder.route()
						.GET("", HANDLER_FUNCTION, builder -> builder.operationId("get-users"))
						.POST("/special", HANDLER_FUNCTION, builder -> builder.operationId("create-user-special"))
						.nest(path("/groups"), routerFunctionSupplier, operationsConsumer)
						.nest(path("/groups2"), routerFunctionSupplier, operationsConsumer)
						.nest(path("/greeter3").or(path("/greeter4")), routerFunctionSupplier, operationsConsumer)
						.build())));

	}

}
