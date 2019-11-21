package test.org.springdoc.api.app1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kotlin.coroutines.Continuation;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class ControllerWithContinuationParameter {

    @Operation(summary = "Ignore Continuation parameter")
    @GetMapping(value = "/test/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<String> get(@PathVariable(value = "id") String key, Continuation continuation) {
        return null;
    }
}
