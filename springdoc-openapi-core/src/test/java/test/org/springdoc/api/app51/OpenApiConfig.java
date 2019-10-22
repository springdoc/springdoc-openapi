package test.org.springdoc.api.app51;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(info = @Info(title = "${info.title}", description = "${info.description}", version = "${info.version}", license = @License(name = "${info.license.name}", url = "${info.license.url}")))
public class OpenApiConfig {
}
