package test.org.springdoc.api.app23;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@SecurityScheme(type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, name = "Authorization", description = "A core-auth Bearer token")
public class HelloController {

    @Operation(summary = "Add a new person to the store", description = "", security = {
            @SecurityRequirement(name = "Authorization")})
    @GetMapping(value = "/persons")
    public void persons(@Valid @NotBlank String name) {

    }

}
