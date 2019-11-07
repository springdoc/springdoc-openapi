package test.org.springdoc.api.app53;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/global")
public class HelloControllerWithGlobalApiResponse {

    @Operation(description = "Some operation", responses = {
            @ApiResponse(responseCode = "204", description = "Explicit description for this response")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping(value = "/hello1", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> listWithNoApiResponse() {
        return null;
    }

    @Operation(description = "Some operation")
    @ApiResponse(responseCode = "200", description = "Explicit description for this response")
    @GetMapping(value = "/hello2", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> listWithDefaultResponseStatus() {
        return null;
    }
}