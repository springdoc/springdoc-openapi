package test.org.springdoc.api.app84;

import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EmployeeRepository {

	static Map<String, Employee> employeeData;

	static Map<String,String> employeeAccessData;


	public Mono<Employee> findEmployeeById(@Parameter(in = ParameterIn.PATH) String id)
	{
		return Mono.just(employeeData.get(id));
	}

	public Flux<Employee> findAllEmployees()
	{
		return Flux.fromIterable(employeeData.values());
	}

	public Mono<Employee> updateEmployee(Employee employee)
	{
		Employee existingEmployee=employeeData.get(employee.getId());
		if(existingEmployee!=null)
		{
			existingEmployee.setName(employee.getName());
		}
		return Mono.just(existingEmployee);
	}
}