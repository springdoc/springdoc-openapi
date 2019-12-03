package test.org.springdoc.api.app63;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@RestController
public class HelloController {

    @GetMapping("/test")
    public void test(HttpSession header, HttpServletRequest request, HttpServletResponse response, Locale locale,
                     String hello) {
    }

}