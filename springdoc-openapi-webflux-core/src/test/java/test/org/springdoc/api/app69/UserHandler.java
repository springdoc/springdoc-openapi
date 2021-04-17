package test.org.springdoc.api.app69;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

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
            .flatMap(customer -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(customer)))
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
            .flatMap(cust -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(cust)));
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
            .flatMap(strMono -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(fromObject(strMono)));
    }

}
