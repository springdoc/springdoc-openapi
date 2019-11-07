package test.org.springdoc.api.app49;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Operation(description = "Obtain the list of services available in the system")
    @ApiResponses({@ApiResponse(responseCode = "401", ref = "Unauthorized")})
    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> list() {
        return null;
    }

}
