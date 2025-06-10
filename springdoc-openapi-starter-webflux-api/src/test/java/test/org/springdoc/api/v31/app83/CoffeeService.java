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

package test.org.springdoc.api.v31.app83;

import java.time.Duration;
import java.time.Instant;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
public class CoffeeService {
	private final CoffeeRepository repo;

	public CoffeeService(CoffeeRepository repo) {
		this.repo = repo;
	}

	Flux<Coffee> getAllCoffees() {
		return repo.findAll();
	}

	Mono<Coffee> getCoffeeById(@Parameter(in = ParameterIn.PATH) String id) {
		return repo.findById(id);
	}

	Flux<CoffeeOrder> getOrdersForCoffeeById(@Parameter(in = ParameterIn.PATH) String id) {
		return Flux.interval(Duration.ofSeconds(1))
				.onBackpressureDrop()
				.map(i -> new CoffeeOrder(id, Instant.now()));
	}
}
