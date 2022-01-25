package test.org.springdoc.api.app85.handler;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class PositionHandler {


    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return null;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
		return null;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
		return null;
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
		return null;
    }

}
