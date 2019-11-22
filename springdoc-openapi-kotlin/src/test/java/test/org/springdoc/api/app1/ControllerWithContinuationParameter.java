package test.org.springdoc.api.app1;

import io.swagger.v3.oas.annotations.Operation;
import kotlin.coroutines.Continuation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ControllerWithContinuationParameter {

    @Operation(summary = "Ignore Continuation parameter")
    @GetMapping(value = "/test/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<String> get(@PathVariable(value = "id") String key, Continuation continuation) {
        return null;
    }
}
