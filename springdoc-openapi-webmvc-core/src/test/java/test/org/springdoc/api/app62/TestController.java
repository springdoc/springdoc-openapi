package test.org.springdoc.api.app62;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@BaseController
@Tag(name = "Test Controller")
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @Operation(summary = "This is the test endpoint")
    public String test(@RequestHeader("Accept") String accept) {
        return "This is a test";
    }
}