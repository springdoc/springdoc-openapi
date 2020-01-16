package test.org.springdoc.api.app15;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;

@TestPropertySource(properties = {"springdoc.operation-descriptions.myOperation=My Desc", "springdoc.openapidefinition.info.title=My title"})
public class SpringDocApp15Test extends AbstractSpringDocTest {

    @SpringBootApplication
    @OpenAPIDefinition(info = @Info(title = "${springdoc.openapidefinition.info.title}"))
    static class SpringDocTestApp { }
}