package test.org.springdoc.api.app18;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
public class HelloController {

    @GetMapping(value = "/persons")
    public String persons(@NotBlank String name) {
        return "OK";
    }

    @GetMapping(value = "/persons2")
    public String persons2(@NotBlank @Parameter(description = "persons name") String name) {
        return "OK";
    }

    @GetMapping(value = "/persons3")
    public String persons3(@NotBlank @Parameter(description = "persons name") @RequestParam String name) {
        return "OK";
    }

}
