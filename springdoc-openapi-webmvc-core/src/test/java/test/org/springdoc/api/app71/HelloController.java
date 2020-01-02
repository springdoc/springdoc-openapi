package test.org.springdoc.api.app71;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Operation(description = "Some operation")
    @GetMapping("/example")
    public String test() {
        return null;
    }
}
