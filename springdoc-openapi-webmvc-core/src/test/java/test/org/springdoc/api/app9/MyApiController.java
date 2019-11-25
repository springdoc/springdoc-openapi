package test.org.springdoc.api.app9;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyApiController implements MyApi {
    public String get(String language) {
        return language;
    }


    @Operation(description = "Annotations from class with hidden parameter code")
    @GetMapping("/getCode")
    public String getCode(@Parameter(hidden = true) String code) {
        return code;
    }
}