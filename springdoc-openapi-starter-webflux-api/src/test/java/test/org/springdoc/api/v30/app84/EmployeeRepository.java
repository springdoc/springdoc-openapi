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

package test.org.springdoc.api.v30.app84;

import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EmployeeRepository {

	static Map<String, Employee> employeeData;

	static Map<String, String> employeeAccessData;


	public Mono<Employee> findEmployeeById(@Parameter(in = ParameterIn.PATH) String id) {
		return Mono.just(employeeData.get(id));
	}

	public Flux<Employee> findAllEmployees() {
		return Flux.fromIterable(employeeData.values());
	}

	public Mono<Employee> updateEmployee(Employee employee) {
		Employee existingEmployee = employeeData.get(employee.getId());
		if (existingEmployee != null) {
			existingEmployee.setName(employee.getName());
		}
		return Mono.just(existingEmployee);
	}
}