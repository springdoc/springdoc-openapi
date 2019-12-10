package test.org.springdoc.api.app59;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Deprecated
    @GetMapping("/example")
    public void test() {
    }
}
