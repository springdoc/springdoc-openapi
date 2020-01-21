package test.org.springdoc.api.app15;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;

@TestPropertySource(properties = {
    "springdoc.operation-descriptions.myOperation=My Desc",
    "springdoc.openapidefinition.info.title=My title",
    "springdoc.openapidefinition.info.desc=My description",
    "springdoc.openapidefinition.info.version=My version",
    "springdoc.openapidefinition.info.terms=My terms",
    "springdoc.openapidefinition.info.license.name=My license name",
    "springdoc.openapidefinition.info.license.url=My license url",
    "springdoc.openapidefinition.info.contact.name=My contact name",
    "springdoc.openapidefinition.info.contact.email=My contact email",
    "springdoc.openapidefinition.info.contact.url=My contact url"
})
public class SpringDocApp15Test extends AbstractSpringDocTest {

    @SpringBootApplication
    @OpenAPIDefinition(info = @Info(
        title = "${springdoc.openapidefinition.info.title}",
        description = "${springdoc.openapidefinition.info.desc}",
        version = "${springdoc.openapidefinition.info.version}",
        termsOfService = "${springdoc.openapidefinition.info.terms}",
        license = @License(
            name = "${springdoc.openapidefinition.info.license.name}",
            url = "${springdoc.openapidefinition.info.license.url}"
        ),
        contact = @Contact(
            name = "${springdoc.openapidefinition.info.contact.name}",
            email = "${springdoc.openapidefinition.info.contact.email}",
            url = "${springdoc.openapidefinition.info.contact.url}"
        )
    ))
    static class SpringDocTestApp { }
}
