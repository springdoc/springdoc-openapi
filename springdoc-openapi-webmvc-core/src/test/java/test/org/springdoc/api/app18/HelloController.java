package test.org.springdoc.api.app18;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

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

    @GetMapping(value = "/persons4")
    public String persons4(@PositiveOrZero int age) {
        return "OK";
    }

    @GetMapping(value = "/persons5")
    public String persons5(@NegativeOrZero int age) {
        return "OK";
    }

    @GetMapping(value = "/persons6")
    public String persons6(@NotEmpty @Parameter(description = "persons name") String name) {
        return "OK";
    }

}
