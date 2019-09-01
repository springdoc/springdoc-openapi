package test.org.springdoc.api.app12;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class HelloController {

	@GetMapping(value = "/persons")
	@Operation(parameters = {
			@Parameter(name = "name", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class)) })
	public String persons() {
		return "OK";
	}
	
}
