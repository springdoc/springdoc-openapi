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

package test.org.springdoc.api.v30.app72.service;

import java.util.Objects;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.org.springdoc.api.v30.app72.entity.Person;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

	public Flux<Person> getAll() {
		return null;
	}

	public Mono<Person> getById(@Parameter(in = ParameterIn.PATH) final String id) {
		return null;
	}

	public Mono update(@Parameter(in = ParameterIn.PATH) final String id, final Person person) {
		return null;
	}

	public Mono save(final Person person) {
		return null;
	}

	public Mono delete(@Parameter(in = ParameterIn.PATH) final String id) {
		final Mono<Person> dbPerson = getById(id);
		if (Objects.isNull(dbPerson)) {
			return Mono.empty();
		}
		return null;
	}
}
