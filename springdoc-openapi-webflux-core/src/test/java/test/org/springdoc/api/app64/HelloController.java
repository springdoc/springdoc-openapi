package test.org.springdoc.api.app64;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/v1/test")
    public void test1(String hello) {
    }

    @GetMapping(value = "/api/balance/abcd")
    @Operation(summary = "This is the test endpoint")
    public String test2( String from) {
        return "This is a fake test";
    }

}