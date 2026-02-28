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

package test.org.springdoc.api.v30.app82;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;


@Component
public class UserHandler {

	private final UserRepository customerRepository;

	public UserHandler(UserRepository repository) {
		this.customerRepository = repository;
	}

	/**
	 * GET ALL Users
	 */
	public Mono<ServerResponse> getAll(ServerRequest request) {
		// fetch all customers from repository
		Flux<User> customers = customerRepository.getAllUsers();

		// build response
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(customers, User.class);
	}

	/**
	 * GET a User by ID
	 */
	public Mono<ServerResponse> getUser(ServerRequest request) {
		// parse path-variable
		long customerId = Long.valueOf(request.queryParam("id").get());

		// build notFound response
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		// get customer from repository
		Mono<User> customerMono = customerRepository.getUserById(customerId);

		// build response
		return customerMono
				.flatMap(customer -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(customer)))
				.switchIfEmpty(notFound);
	}

	/**
	 * POST a User
	 */
	public Mono<ServerResponse> postUser(ServerRequest request) {
		Mono<User> customer = request.bodyToMono(User.class);
		return ServerResponse.ok().build(customerRepository.saveUser(customer));
	}

	/**
	 * PUT a User
	 */
	public Mono<ServerResponse> putUser(ServerRequest request) {
		// parse id from path-variable
		long customerId = Long.valueOf(request.pathVariable("id"));

		// get customer data from request object
		Mono<User> customer = request.bodyToMono(User.class);

		// get customer from repository
		Mono<User> responseMono = customerRepository.putUser(customerId, customer);

		// build response
		return responseMono
				.flatMap(cust -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(cust)));
	}

	/**
	 * DELETE a User
	 */
	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		// parse id from path-variable
		long customerId = Long.valueOf(request.pathVariable("id"));

		// get customer from repository
		Mono<String> responseMono = customerRepository.deleteUser(customerId);

		// build response
		return responseMono
				.flatMap(strMono -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(fromValue(strMono)));
	}

}
