package test.org.springdoc.api.app2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping
    public Object doPost(@RequestBody String req, org.springframework.security.core.Authentication auth) {
        return null;
    }

}