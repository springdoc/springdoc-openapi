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

package test.org.springdoc.api.v30.app80;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class BookRouter {

	@Bean
	@RouterOperations({
			@RouterOperation(path = "/greeter/greeter2/books", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/greeter/greeter2/books", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_XML_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/greeter/greeter2/books/{author}", beanClass = BookRepository.class, beanMethod = "findByAuthor",
					operation = @Operation(operationId = "findByAuthor"
							, parameters = { @Parameter(in = ParameterIn.PATH, name = "author") })) })
	RouterFunction<?> routes(BookRepository br) {
		return
				nest(path("/greeter").and(path("/greeter2")),
						route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll(), Book.class))
								.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll(), Book.class)))
								.andRoute(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class)));
	}

	@Bean
	@RouterOperations({
			@RouterOperation(path = "/books", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/books/{author}", beanClass = BookRepository.class, beanMethod = "findByAuthor",
					operation = @Operation(operationId = "findByAuthor"
							, parameters = { @Parameter(in = ParameterIn.PATH, name = "author") })) })
	RouterFunction<?> routes1(BookRepository br) {
		return
				nest(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML),
						route(GET("/books"), req -> ok().body(br.findAll(), Book.class))
								.andRoute(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class)));
	}

	@Bean
	@RouterOperations({
			@RouterOperation(path = "/greeter/books", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/greeter/books", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_XML_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/greeter/books/{author}", beanClass = BookRepository.class, beanMethod = "findByAuthor",
					operation = @Operation(operationId = "findByAuthor"
							, parameters = { @Parameter(in = ParameterIn.PATH, name = "author") })),
			@RouterOperation(path = "/greeter2/books", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/greeter2/books", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_XML_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/greeter2/books/{author}", beanClass = BookRepository.class, beanMethod = "findByAuthor",
					operation = @Operation(operationId = "findByAuthor"
							, parameters = { @Parameter(in = ParameterIn.PATH, name = "author") })) })
	RouterFunction<?> routes3(BookRepository br) {
		return
				nest(path("/greeter").or(path("/greeter2")),
						route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll(), Book.class))
								.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll(), Book.class)))
								.andRoute(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class)));
	}

	@Bean
	@RouterOperations({
			@RouterOperation(path = "/test/greeter/greeter2/books", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/test/greeter/greeter2/books", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_XML_VALUE }, beanClass = BookRepository.class, beanMethod = "findAll"),
			@RouterOperation(path = "/test/greeter/greeter2/books/{author}", beanClass = BookRepository.class, beanMethod = "findByAuthor",
					operation = @Operation(operationId = "findByAuthor"
							, parameters = { @Parameter(in = ParameterIn.PATH, name = "author") })) })
	RouterFunction<?> routes4(BookRepository br) {
		return
				nest(path("/test"),
						nest(path("/greeter").and(path("/greeter2")),
								route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll(), Book.class))
										.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll(), Book.class)))
										.andRoute(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author")), Book.class))));
	}
}