package test.org.springdoc.api.app26;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping(value = "/persons")
    public MyModel persons(MyModel myModel) {
        return new MyModel();
    }

}
