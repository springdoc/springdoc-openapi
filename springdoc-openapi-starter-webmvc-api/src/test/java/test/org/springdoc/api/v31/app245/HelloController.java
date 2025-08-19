package test.org.springdoc.api.v31.app245;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "My App",
        version = "${springdoc.version}",
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name = "x-build-time", value = "${git.build.time}")
            })
        }
    )
)
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
