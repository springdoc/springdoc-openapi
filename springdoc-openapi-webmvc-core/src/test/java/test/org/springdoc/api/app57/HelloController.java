package test.org.springdoc.api.app57;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/{name:.+}")
    public ResponseEntity<String> getText(@Parameter(description = "desc", required = true) @PathVariable String name) {
        return null;
    }

}
