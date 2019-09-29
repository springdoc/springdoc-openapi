package test.org.springdoc.api.app41;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@Tag(description = "${swagger.property.parameter.test}", name = "${swagger.property.parameter.test}")
public class HelloController {

	@RequestMapping(value = "/iae_error", method = RequestMethod.GET)
	@Operation(description = "${swagger.property.parameter.test}")
	public ObjectNode getStartFormProperties(@Parameter(description = "${swagger.property.parameter.test}") String test) {
		return null;
	}
}
