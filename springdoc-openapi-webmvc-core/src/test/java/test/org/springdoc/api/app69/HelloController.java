package test.org.springdoc.api.app69;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
public class HelloController {

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    private Callable<ResponseEntity<PersonDTO>> getTasks(String str) {
        return null;
    }

}