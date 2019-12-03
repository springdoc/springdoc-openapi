package test.org.springdoc.api.app1;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class HelloController {

    @GetMapping(value = "/search", produces = {"application/xml", "application/json"})
    public ResponseEntity<List<PersonDTO>> getAllPets(@NotNull Pageable pageable) {
        System.out.println(pageable);
        return null;
    }

}
