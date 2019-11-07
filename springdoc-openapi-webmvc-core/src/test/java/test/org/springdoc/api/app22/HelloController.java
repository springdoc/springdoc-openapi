package test.org.springdoc.api.app22;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {


    @GetMapping(value = "/persons")
    public ResponseEntity<List<List<PersonDTO>>> doGet() {
        return null;
    }


}
