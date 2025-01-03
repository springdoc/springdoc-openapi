package test.org.springdoc.api.v31.app236;


import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping(path = "/testOne", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> testOne(@ParameterObject @Valid ClassOne test) {
        return ResponseEntity.ok("ok");
    }

    @PostMapping(path = "/testTwo", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> testTwo(@RequestBody ClassOne test) {
        return ResponseEntity.ok("ok");
    }
}
