package test.org.springdoc.api.app21;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@SecurityScheme(name = "personstore_auth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "http://personstore.swagger.io/oauth/dialog", scopes = {
        @OAuthScope(name = "write:persons", description = "modify persons in your account"),
        @OAuthScope(name = "read:persons", description = "read your persons")})))
public class HelloController {

    @Operation(summary = "Add a new person to the store", description = "", security = {
            @SecurityRequirement(name = "personstore_auth", scopes = {"write:persons", "read:persons"})}, tags = {
            "person"})
    @GetMapping(value = "/persons")
    public void persons(@Valid @NotBlank String name) {

    }

}
