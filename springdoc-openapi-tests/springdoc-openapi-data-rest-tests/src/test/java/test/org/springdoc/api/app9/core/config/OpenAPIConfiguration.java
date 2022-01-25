package test.org.springdoc.api.app9.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.stereotype.Component;

@Component
@OpenAPIDefinition( //
		info = @Info( //
				title = "Demo Springdoc Issue #401", //
				version = "0.0.1-SNAPSHOT", //
				description = "A demo API to illustrate Springdoc Issue #401." //
		) //
)
public class OpenAPIConfiguration {
	// NO-OP
}
