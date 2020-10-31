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

package test.org.springdoc.api.app141;

import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;

import static org.springdoc.core.Constants.OPERATION_ATTRIBUTE;
import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.nest;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@Configuration
class BookRouter {


	@Bean
	RouterFunction<?> routes(BookRepository br) {
		return nest(path("/greeter").and(path("/greeter2")),
				route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll()))
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(BookRepository.class).beanMethod("findAll"))

						.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll()))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(BookRepository.class).beanMethod("findAll")))

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author"))))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder()
										.operationId("findByAuthor").parameter(parameterBuilder().name("author").in(ParameterIn.PATH))
										.beanClass(BookRepository.class).beanMethod("findByAuthor")))
		);
	}

	@Bean
	RouterFunction<?> routes1(BookRepository br) {
		return nest(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML),
				route(GET("/books"), req -> ok().body(br.findAll()))
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(BookRepository.class).beanMethod("findAll"))

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author"))))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder()
										.operationId("findByAuthor").parameter(parameterBuilder().name("author").in(ParameterIn.PATH))
										.beanClass(BookRepository.class).beanMethod("findByAuthor"))));
	}

	@Bean
	RouterFunction<?> routes3(BookRepository br) {
		return nest(path("/greeter").or(path("/greeter2")),
				route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll()))
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(BookRepository.class).beanMethod("findAll"))


						.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll()))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(BookRepository.class).beanMethod("findAll")))

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author"))))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder()
										.operationId("findByAuthor").parameter(parameterBuilder().name("author").in(ParameterIn.PATH))
										.beanClass(BookRepository.class).beanMethod("findByAuthor"))));
	}

	@Bean
	RouterFunction<?> routes4(BookRepository br) {
		return nest(path("/test"), nest(path("/greeter").and(path("/greeter2")),
				route(GET("/books").and(accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)), req -> ok().body(br.findAll()))
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(BookRepository.class).beanMethod("findAll"))

						.and(route(GET("/books").and(accept(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)), req -> ok().body(br.findAll()))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(BookRepository.class).beanMethod("findAll")))

						.and(route(GET("/books/{author}"), req -> ok().body(br.findByAuthor(req.pathVariable("author"))))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder()
										.operationId("findByAuthor").parameter(parameterBuilder().name("author").in(ParameterIn.PATH))
										.beanClass(BookRepository.class).beanMethod("findByAuthor")))));
	}
}