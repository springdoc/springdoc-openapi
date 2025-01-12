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
package test.org.springdoc.api.v31.app4;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Spring Web {@link RestController} used to generate a REST API.
 *
 * @author Greg Turnquist
 */
@RestController
class EmployeeController {

	private final EmployeeRepository repository;

	EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	/**
	 * Look up all employees, and transform them into a REST collection resource. Then return them through Spring Web's
	 * {@link ResponseEntity} fluent API.
	 */
	@GetMapping("/employees")
	ResponseEntity<CollectionModel<EntityModel<Employee>>> findAll() {

		List<EntityModel<Employee>> employees = StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(employee -> EntityModel.of(employee, //
						linkTo(methodOn(EmployeeController.class).findOne(employee.getId())).withSelfRel(), //
						linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"))) //
				.toList();

		return ResponseEntity.ok( //
				CollectionModel.of(employees, //
						linkTo(methodOn(EmployeeController.class).findAll()).withSelfRel()));
	}

	@PostMapping("/employees")
	ResponseEntity<EntityModel<Employee>> newEmployee(@RequestBody Employee employee) {

		try {
			Employee savedEmployee = repository.save(employee);

			EntityModel<Employee> employeeResource = EntityModel.of(savedEmployee, //
					linkTo(methodOn(EmployeeController.class).findOne(savedEmployee.getId())).withSelfRel());

			return ResponseEntity //
					.created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref())) //
					.body(employeeResource);
		}
		catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	/**
	 * Look up a single {@link Employee} and transform it into a REST resource. Then return it through Spring Web's
	 * {@link ResponseEntity} fluent API.
	 *
	 * @param id
	 */
	@GetMapping("/employees/{id}")
	ResponseEntity<EntityModel<Employee>> findOne(@PathVariable long id) {

		return repository.findById(id) //
				.map(employee -> EntityModel.of(employee, //
						linkTo(methodOn(EmployeeController.class).findOne(employee.getId())).withSelfRel(), //
						linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"))) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Update existing employee then return a Location header.
	 *
	 * @param employee
	 * @param id
	 * @return
	 */
	@PutMapping("/employees/{id}")
	ResponseEntity<Void> updateEmployee(@RequestBody Employee employee, @PathVariable long id) throws URISyntaxException {

		Employee employeeToUpdate = employee;
		employeeToUpdate.setId(id);
		repository.save(employeeToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(EmployeeController.class).findOne(id)).withSelfRel();

		return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();

	}

}
