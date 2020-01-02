package test.org.springdoc.api.app71;

import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;

@TestPropertySource(properties = "springdoc.protocol-relative-baseurl=true")
public class SpringDocApp71Test extends AbstractSpringDocTest {
}
