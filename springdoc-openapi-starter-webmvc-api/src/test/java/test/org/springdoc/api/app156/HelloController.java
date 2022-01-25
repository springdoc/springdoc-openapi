package test.org.springdoc.api.app156;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RestController
public class HelloController {
    @GetMapping("/hello")
    @Parameter(name = "someEnums", in = QUERY, description = "SomeEum decs",
           array = @ArraySchema(schema = @Schema(implementation = SomeEnum.class)))
    @Parameter(name = "textSet", in = QUERY, description = "First decs",
            array = @ArraySchema(schema = @Schema(implementation = String.class)))
    @Parameter(name = "someText", in = QUERY, description = "Second decs",
           schema = @Schema(type = "string"))
    public String hello(@Parameter(hidden = true) User user) {
        String forReturn = "Hello ";
        StringBuilder stringBuilder = new StringBuilder(forReturn);

        if (user.getSomeEnums() != null) {
            for (SomeEnum some : user.getSomeEnums()) {
                stringBuilder.append(some);
                stringBuilder.append(" ");
            }
        }

        if (user.getSomeText() != null) {
            for (String text : user.getTextSet()) {
                stringBuilder.append(text);
                stringBuilder.append(" ");
            }
        }

        if (user.getSomeText() != null) {
            stringBuilder.append(user.getSomeText());
        }

        return stringBuilder.toString();
    }
}
