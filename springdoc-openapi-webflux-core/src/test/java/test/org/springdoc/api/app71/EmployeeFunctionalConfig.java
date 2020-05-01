package test.org.springdoc.api.app71;

import org.springdoc.webflux.annotations.RouterOperation;
import org.springdoc.webflux.annotations.RouterOperations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class EmployeeFunctionalConfig {


	@Bean
	EmployeeRepository employeeRepository() {
		return new EmployeeRepository();
	}

	@Bean
	@RouterOperation(path = "/employees", method = RequestMethod.GET, beanClass = EmployeeRepository.class, beanMethod = "findAllEmployees")
	RouterFunction<ServerResponse> getAllEmployeesRoute() {
		return route(GET("/employees"),
				req -> ok().body(
						employeeRepository().findAllEmployees(), Employee.class));
	}

	@Bean
	@RouterOperation(path = "/employees/{id}", method = RequestMethod.GET, beanClass = EmployeeRepository.class, beanMethod = "findEmployeeById")
	RouterFunction<ServerResponse> getEmployeeByIdRoute() {
		return route(GET("/employees/{id}"),
				req -> ok().body(
						employeeRepository().findEmployeeById(req.pathVariable("id")), Employee.class));
	}

	@Bean
	@RouterOperation(path = "/employees/update", method = RequestMethod.POST, beanClass = EmployeeRepository.class, beanMethod = "updateEmployee")
	RouterFunction<ServerResponse> updateEmployeeRoute() {
		return route(POST("/employees/update"),
				req -> req.body(BodyExtractors.toMono(Employee.class))
						.doOnNext(employeeRepository()::updateEmployee)
						.then(ok().build()));
	}

	@Bean
	@RouterOperations({ @RouterOperation(path = "/employees-composed/update", method = RequestMethod.POST, beanClass = EmployeeRepository.class, beanMethod = "updateEmployee"),
			@RouterOperation(path = "/employees-composed/{id}", method = RequestMethod.GET, beanClass = EmployeeRepository.class, beanMethod = "findEmployeeById"),
			@RouterOperation(path = "/employees-composed", method = RequestMethod.GET, beanClass = EmployeeRepository.class, beanMethod = "findAllEmployees") })
	RouterFunction<ServerResponse> composedRoutes() {
		return
				route(GET("/employees-composed"),
						req -> ok().body(
								employeeRepository().findAllEmployees(), Employee.class))
						.and(route(GET("/employees-composed/{id}"),
								req -> ok().body(
										employeeRepository().findEmployeeById(req.pathVariable("id")), Employee.class)))
						.and(route(POST("/employees-composed/update"),
								req -> req.body(BodyExtractors.toMono(Employee.class))
										.doOnNext(employeeRepository()::updateEmployee)
										.then(ok().build())));
	}

}