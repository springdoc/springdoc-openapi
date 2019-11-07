package test.org.springdoc.api.app39;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Operation(summary = "test Request")
    @RequestBody(description = "test value", required = true, content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/test")
    public void searchEmployee(String test) {
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
