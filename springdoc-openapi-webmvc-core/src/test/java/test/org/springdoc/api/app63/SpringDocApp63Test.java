package test.org.springdoc.api.app63;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.org.springdoc.api.AbstractSpringDocTest;

public class SpringDocApp63Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("springdoc.packagesToScan", "hell,hello1, hello.me");
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("springdoc.packagesToScan");
    }
}
