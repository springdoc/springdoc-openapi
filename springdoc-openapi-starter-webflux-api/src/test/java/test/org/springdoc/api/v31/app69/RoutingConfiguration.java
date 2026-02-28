/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31.app69;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoutingConfiguration {

	@Bean
	@RouterOperations({ @RouterOperation(path = "/api/user/index", beanClass = UserRepository.class, beanMethod = "getAllUsers"),
			@RouterOperation(path = "/api/user/{id}", beanClass = UserRepository.class, beanMethod = "getUserById"),
			@RouterOperation(path = "/api/user/post", beanClass = UserRepository.class, beanMethod = "saveUser"),
			@RouterOperation(path = "/api/user/put/{id}", beanClass = UserRepository.class, beanMethod = "putUser"),
			@RouterOperation(path = "/api/user/delete/{id}", beanClass = UserRepository.class, beanMethod = "deleteUser") })
	public RouterFunction<ServerResponse> monoRouterFunction(UserHandler userHandler) {
		return route(GET("/api/user/index").and(accept(MediaType.APPLICATION_JSON)), userHandler::getAll)
				.andRoute(GET("/api/user/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUser)
				.andRoute(POST("/api/user/post").and(accept(MediaType.APPLICATION_JSON)), userHandler::postUser)
				.andRoute(PUT("/api/user/put/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::putUser)
				.andRoute(DELETE("/api/user/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::deleteUser);
	}

}