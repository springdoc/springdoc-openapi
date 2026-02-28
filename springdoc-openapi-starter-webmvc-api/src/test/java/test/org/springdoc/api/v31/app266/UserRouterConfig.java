/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app266;

import test.org.springdoc.api.v31.app266.user.UserDTOv1;
import test.org.springdoc.api.v31.app266.user.UserDTOv2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.webmvc.core.fn.SpringdocRouteBuilder.route;

/**
 * The type User router config.
 * Uses the springdoc-openapi DSL without beanClass/beanMethod.
 *
 * @author bnasslahsen
 */
@Configuration
public class UserRouterConfig {

	/**
	 * Routes router function.
	 * @param handler the handler
	 * @return the router function
	 */
	@Bean
	RouterFunction<ServerResponse> routes(UserHandler handler) {
		return route()
				.GET("/api/users/media", RequestPredicates.version("1.0").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
						handler::getUsersMediaV1,
						ops -> ops.operationId("getUsersMediaV1")
								.response(responseBuilder().responseCode("200").implementationArray(UserDTOv1.class)).build())
				.GET("/api/users/media", RequestPredicates.version("2.0").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
						handler::getUsersMediaV2,
						ops -> ops.operationId("getUsersMediaV2")
								.response(responseBuilder().responseCode("200").implementationArray(UserDTOv2.class)).build())
				.build();
	}

}
