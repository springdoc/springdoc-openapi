package test.org.springdoc.api.app15;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;

@TestPropertySource(properties = "springdoc.operation-descriptions.myOperation=My Desc")
public class SpringDocApp15Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }
}