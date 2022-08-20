package test.org.springdoc.api.v30.app193;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class BasicController {

    @GetMapping("/test")
    @Operation(summary = "get", description = "Provides an animal.")
    public Animal get() {

        return new Dog("Foo", 12);
    }
}
