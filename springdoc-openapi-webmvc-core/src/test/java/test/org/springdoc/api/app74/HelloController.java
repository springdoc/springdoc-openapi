package test.org.springdoc.api.app74;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @PostMapping("/test")
    @RequestBody(
            content = @Content(
                    examples = @ExampleObject(
                            value = "sample"
                    )
            )
    )
    public String postMyRequestBody(
            String myRequestBody) {
        return null;
    }

}
