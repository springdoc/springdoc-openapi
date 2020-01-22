package test.org.springdoc.api.app64;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.org.springdoc.api.AbstractSpringDocTest;

public class SpringDocApp64Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @BeforeAll
    public static void beforeClass() {
        System.setProperty("springdoc.paths-to-match", "/v1, /api/**");
    }

    @AfterAll
    public static void afterClass() {
        System.clearProperty("springdoc.paths-to-match");
    }
}
