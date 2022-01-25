package test.org.springdoc.api.app85.service;

import java.util.Objects;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.org.springdoc.api.app85.entity.Person;

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
