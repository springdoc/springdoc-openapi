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

package test.org.springdoc.api.v30.app85.controller;

import test.org.springdoc.api.v30.app85.handler.PersonHandler;
import test.org.springdoc.api.v30.app85.service.PersonService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.utils.Constants.OPERATION_ATTRIBUTE;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class PersonRouter {

	@Bean
	public RouterFunction<ServerResponse> personRoute(PersonHandler handler) {
		return route(GET("/getAllPersons").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(PersonService.class).beanMethod("getAll"))

				.and(route(GET("/getPerson/{id}").and(accept(MediaType.APPLICATION_OCTET_STREAM)), handler::findById)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(PersonService.class).beanMethod("getById")))

				.and(route(POST("/createPerson").and(accept(MediaType.APPLICATION_JSON)), handler::save)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(PersonService.class).beanMethod("save")))

				.and(route(DELETE("/deletePerson/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(PersonService.class).beanMethod("delete")));
	}

}
