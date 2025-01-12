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

package test.org.springdoc.api.v30.app83;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.utils.Constants.OPERATION_ATTRIBUTE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfig {
	private final CoffeeService service;

	public RouteConfig(CoffeeService service) {
		this.service = service;
	}

	@Bean
	RouterFunction<ServerResponse> routerFunction() {
		return route(GET("/coffees"), this::all)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(CoffeeService.class).beanMethod("getAllCoffees"))

				.and(route(GET("/coffees/{id}"), this::byId)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(CoffeeService.class).beanMethod("getCoffeeById")))

				.and(route(GET("/coffees/{id}/orders"), this::orders)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(CoffeeService.class).beanMethod("getOrdersForCoffeeById")));
	}

	private Mono<ServerResponse> all(ServerRequest req) {
		return ServerResponse.ok()
				.body(service.getAllCoffees(), Coffee.class);
	}

	private Mono<ServerResponse> byId(ServerRequest req) {
		return ServerResponse.ok()
				.body(service.getCoffeeById(req.pathVariable("id")), Coffee.class);
	}

	private Mono<ServerResponse> orders(ServerRequest req) {
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(service.getOrdersForCoffeeById(req.pathVariable("id")), CoffeeOrder.class);
	}
}
