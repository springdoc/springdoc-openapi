package test.org.springdoc.api.app191.Handler;

import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class GetNameHandler extends BaseHandler{
    @Override
    protected Mono<ServerResponse> apply(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("Name API is called");
    }

}
