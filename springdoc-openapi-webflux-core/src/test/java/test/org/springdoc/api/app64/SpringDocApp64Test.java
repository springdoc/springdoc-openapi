package test.org.springdoc.api.app64;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import test.org.springdoc.api.AbstractSpringDocTest;

public class SpringDocApp64Test extends AbstractSpringDocTest {

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("springdoc.pathsToMatch", "/v1, /api/**");
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("springdoc.pathsToMatch");
    }
}
