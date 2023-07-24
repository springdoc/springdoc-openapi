package test.org.springdoc.api.v30.app210;

import org.springdoc.core.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.v30.AbstractSpringDocV30Test;


@TestPropertySource(properties = {
        Constants.SPRINGDOC_SAME_MODEL_NAME_CONVERTER_ENABLED + "=true",
        "springdoc.group-configs[0].group=first",
        "springdoc.group-configs[0].packages-to-scan=test.org.springdoc.api.v30.app210"
})
public class SpringDocApp210Test extends AbstractSpringDocV30Test {

    @SpringBootApplication
    static class SpringDocTestApp {
    }

}
