package test.org.springdoc.api.v31.app237;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import test.org.springdoc.api.v31.app237.dto.Example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnwrappedController {

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Simple get task")
    @ApiResponse(responseCode = "200", description = "Task has been started")
    @ApiResponse(responseCode = "404", description = "Task was not found")
    @ApiResponse(responseCode = "409", description = "Task is already running")
    public Example exampleGet() {
        return new Example(new Example.Wrapped("Some value"), 1);
    }
}
