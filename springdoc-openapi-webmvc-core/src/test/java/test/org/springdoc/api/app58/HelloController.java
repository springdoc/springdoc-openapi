package test.org.springdoc.api.app58;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/examplePost")
    @Operation(summary = "schema example")
    public Object example(@Parameter(schema = @Schema(hidden = true)) JsonNode json) {
        return null;
    }

    @GetMapping("/example")
    public void test(@Parameter(schema = @Schema(hidden = true)) JsonNode json) {

    }
}
