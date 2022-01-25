package test.org.springdoc.api.app85.handler;

import reactor.core.publisher.Mono;
import test.org.springdoc.api.app85.entity.Person;
import test.org.springdoc.api.app85.service.PersonService;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@SuppressWarnings({"deprecared","unchecked"})
public class PersonHandler {

    private final PersonService personService;

    public PersonHandler(PersonService personService) {
        this.personService = personService;
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personService.getById(id), Person.class);
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personService.getAll(), Person.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<Person> person = request.bodyToMono(Person.class);
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(person.flatMap(personService::save), Person.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personService.delete(id), Void.class);
    }

}
