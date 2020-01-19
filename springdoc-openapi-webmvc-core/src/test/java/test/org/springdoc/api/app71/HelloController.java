package test.org.springdoc.api.app71;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/persons")
    public String persons(Dog dog) {
        return null;
    }


}