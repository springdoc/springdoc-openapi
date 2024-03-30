package test.org.springdoc.api.app191.Handler;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public abstract class BaseHandler {
    protected abstract Mono<ServerResponse> apply (ServerRequest serverRequest);

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return this.apply(serverRequest);
    }
}
