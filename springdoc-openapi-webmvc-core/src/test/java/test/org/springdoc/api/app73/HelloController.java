package test.org.springdoc.api.app73;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.org.springdoc.api.app71.Dog;


@RestController
@RequestMapping({"/{country_code}/persons/", "/persons"})
public class HelloController {

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(name = "country_code", in = ParameterIn.PATH) String countryCode, @PathVariable("id") String id) {

    }

    @GetMapping("/{id}")
    public String get(@Parameter(name = "country_code", in = ParameterIn.PATH) String countryCode, @PathVariable("id") String id) {
        return null;
    }
}
