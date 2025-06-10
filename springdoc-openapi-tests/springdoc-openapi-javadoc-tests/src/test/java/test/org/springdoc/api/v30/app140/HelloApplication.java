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

package test.org.springdoc.api.v30.app140;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.utils.Constants.OPERATION_ATTRIBUTE;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

/**
 * The type Hello application.
 */
@Configuration
class HelloApplication {

	/**
	 * Filter server response.
	 *
	 * @param serverRequest   the server request
	 * @param handlerFunction the handler function
	 * @return the server response
	 * @throws Exception the exception
	 */
	private static ServerResponse filter(ServerRequest serverRequest, HandlerFunction<ServerResponse> handlerFunction) throws Exception {
		return handlerFunction.handle(serverRequest);
	}

	/**
	 * Routes router function.
	 *
	 * @param ph the ph
	 * @return the router function
	 */
	@Bean
	RouterFunction<ServerResponse> routes(PersonHandler ph) {
		String root = "";
		return route()
				.GET(root + "/people", ph::handleGetAllPeople)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(PersonService.class).beanMethod("all"))
				.GET(root + "/people/{id}", ph::handleGetPersonById)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(PersonService.class).beanMethod("byId"))
				.POST(root + "/people", ph::handlePostPerson)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(PersonService.class).beanMethod("save"))
				.filter(HelloApplication::filter)
				.build();
	}
}

/**
 * The type Simple filter.
 */
@Component
class SimpleFilter extends GenericFilter {

	/**
	 * Do filter.
	 *
	 * @param req         the req
	 * @param res         the res
	 * @param filterChain the filter chain
	 * @throws IOException      the io exception
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(req, res);
	}
}

/**
 * The type Person handler.
 */
@Component
class PersonHandler {

	/**
	 * The Person service.
	 */
	private final PersonService personService;

	/**
	 * Instantiates a new Person handler.
	 *
	 * @param personService the person service
	 */
	PersonHandler(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * Handle get all people server response.
	 *
	 * @param serverRequest the server request
	 * @return the server response
	 */
	ServerResponse handleGetAllPeople(ServerRequest serverRequest) {
		return ok().body(personService.all());
	}

	/**
	 * Handle post person server response.
	 *
	 * @param r the r
	 * @return the server response
	 * @throws ServletException the servlet exception
	 * @throws IOException      the io exception
	 */
	ServerResponse handlePostPerson(ServerRequest r) throws ServletException, IOException {
		Person result = personService.save(new Person(null, r.body(Person.class).getName()));
		URI uri = URI.create("/people/" + result.getId());
		return ServerResponse.created(uri).body(result);
	}

	/**
	 * Handle get person by id server response.
	 *
	 * @param r the r
	 * @return the server response
	 */
	ServerResponse handleGetPersonById(ServerRequest r) {
		return ok().body(personService.byId(Long.parseLong(r.pathVariable("id"))));
	}
}

/**
 * The type Greetings rest controller.
 */
@RestController
class GreetingsRestController {

	/**
	 * Greet string.
	 *
	 * @param name the name
	 * @return the string
	 */
	@GetMapping("/greet/{name}")
	String greet(@PathVariable String name) {
		return "hello " + name + "!";
	}
}

/**
 * The type Person service.
 */
@Service
class PersonService {

	/**
	 * The Counter.
	 */
	private final AtomicLong counter = new AtomicLong();

	/**
	 * The People.
	 */
	private final Set<Person> people = Stream.of(
					new Person(counter.incrementAndGet(), "Jane"),
					new Person(counter.incrementAndGet(), "Josh"),
					new Person(counter.incrementAndGet(), "Gordon"))
			.collect(Collectors.toCollection(HashSet::new));


	/**
	 * Save person.
	 *
	 * @param p the p
	 * @return the person
	 */
	Person save(Person p) {
		Person person = new Person(counter.incrementAndGet(), p.getName());
		this.people.add(person);
		return person;
	}

	/**
	 * All set.
	 *
	 * @return the set
	 */
	Set<Person> all() {
		return this.people;
	}

	/**
	 * By id person.
	 *
	 * @param id the id
	 * @return the person
	 */
	Person byId(@Parameter(in = ParameterIn.PATH) Long id) {
		return this.people.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("no " + Person.class.getName() + " with that ID found!"));
	}

}

/**
 * The type Person.
 */
class Person {

	/**
	 * The Id.
	 */
	private Long id;

	/**
	 * The Name.
	 */
	private String name;

	/**
	 * Instantiates a new Person.
	 *
	 * @param id   the id
	 * @param name the name
	 */
	public Person(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
