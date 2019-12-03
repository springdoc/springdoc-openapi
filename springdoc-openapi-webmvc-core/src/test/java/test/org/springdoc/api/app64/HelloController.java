package test.org.springdoc.api.app64;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

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