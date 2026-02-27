/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v31.app265;

import test.org.springdoc.api.v31.app265.user.UserDTOv1;
import test.org.springdoc.api.v31.app265.user.UserDTOv2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
				.GET("/api/users/list", RequestPredicates.version("1.0"), handler::findAllV1,
						ops -> ops.operationId("getUsersV1")
								.response(responseBuilder().responseCode("200").implementationArray(UserDTOv1.class)).build())
				.GET("/api/users/list", RequestPredicates.version("v2"), handler::findAllV2,
						ops -> ops.operationId("getUsersV2")
								.response(responseBuilder().responseCode("200").implementationArray(UserDTOv2.class)).build())
				.build();
	}

}
