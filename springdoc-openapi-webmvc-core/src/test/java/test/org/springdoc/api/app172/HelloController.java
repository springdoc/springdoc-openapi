package test.org.springdoc.api.app172;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import test.org.springdoc.api.app172.annotation.MyIdPathVariable;
import test.org.springdoc.api.app172.model.MyObj;

@RestController
public class HelloController {
    @GetMapping("/test/{objId}")
    String test(@MyIdPathVariable MyObj obj) {
        return obj.getContent();
    }
}
