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

package test.org.springdoc.api.app89;

import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springdoc.core.Constants.OPERATION_ATTRIBUTE;
import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class BookRouter {

	@Bean
	RouterFunction<?> routes(BookRepository br) {
		return nest(path("/greeter").and(path("/greeter2")),
				route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll(), Book.class))
						.withAttribute(OPERATION_ATTRIBUTE, getFindAll())

						.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll(), Book.class))
								.withAttribute(OPERATION_ATTRIBUTE, getFindAll()))

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class))
								.withAttribute(OPERATION_ATTRIBUTE, getRouterAttribute()))
		);
	}

	@Bean
	RouterFunction<?> routes1(BookRepository br) {
		return nest(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML),
				route(GET("/books"), req -> ok().body(br.findAll(), Book.class))
						.withAttribute(OPERATION_ATTRIBUTE, getFindAll())

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class))
								.withAttribute(OPERATION_ATTRIBUTE, getRouterAttribute())));
	}

	@Bean
	RouterFunction<?> routes3(BookRepository br) {
		return nest(path("/greeter").or(path("/greeter2")),
				route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll(), Book.class))
						.withAttribute(OPERATION_ATTRIBUTE, getFindAll())


						.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll(), Book.class))
								.withAttribute(OPERATION_ATTRIBUTE, getFindAll()))

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class))
								.withAttribute(OPERATION_ATTRIBUTE, getRouterAttribute())));
	}

	@Bean
	RouterFunction<?> routes4(BookRepository br) {
		return nest(path("/test"), nest(path("/greeter").and(path("/greeter2")),
				route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll(), Book.class))
						.withAttribute(OPERATION_ATTRIBUTE, getFindAll())

						.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll(), Book.class))
								.withAttribute(OPERATION_ATTRIBUTE, getFindAll()))

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class))
								.withAttribute(OPERATION_ATTRIBUTE, getRouterAttribute()))));
	}

	private org.springdoc.core.fn.builders.operation.Builder getFindAll() {
		return operationBuilder().beanClass(BookRepository.class).beanMethod("findAll");
	}

	private org.springdoc.core.fn.builders.operation.Builder getRouterAttribute() {
		return operationBuilder()
				.operationId("findByAuthor").parameter(parameterBuilder().name("author").in(ParameterIn.PATH))
				.beanClass(BookRepository.class).beanMethod("findByAuthor");
	}
}