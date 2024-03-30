package test.org.springdoc.api.app191.Router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import test.org.springdoc.api.app191.Handler.GetNameHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class GetNameRouter {

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(path = "/v1/name", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET,
                            beanClass = GetNameHandler.class, beanMethod = "handle", operation = @Operation(operationId = "getName",
                            description = "get name", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class)))})
                    )
            })

    public RouterFunction<ServerResponse> routerFunction(GetNameHandler getNameHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/v1/name"), getNameHandler::handle);
    }

}
