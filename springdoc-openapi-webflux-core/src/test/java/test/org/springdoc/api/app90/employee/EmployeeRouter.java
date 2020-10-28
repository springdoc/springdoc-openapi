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

package test.org.springdoc.api.app90.employee;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import test.org.springdoc.api.app84.Employee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static test.org.springdoc.api.AbstractSpringDocTest.HANDLER_FUNCTION;

@Configuration
class EmployeeRouter {

	@Bean
	RouterFunction<ServerResponse> getEmployeeByIdRoute() {
		return route().GET("/employees/{id}", HANDLER_FUNCTION, ops -> ops.tag("employee")
				.operationId("findEmployeeById").summary("Find purchase order by ID").tags(new String[] { "MyEmployee" })
				.parameter(parameterBuilder().in(ParameterIn.PATH).name("id").description("Employee Id"))
				.response(responseBuilder().responseCode("200").description("successful operation").implementation(Employee.class))
				.response(responseBuilder().responseCode("400").description("Invalid Employee ID supplied"))
				.response(responseBuilder().responseCode("404").description("Employee not found"))).build();
	}

}