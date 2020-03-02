package test.org.springdoc.api.app95;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class HelloController {

	@GetMapping
	@Operation(summary = "${test.app95.operation.persons.summary}",
			description = "${test.app95.operation.persons.description}")
	public void persons(@Parameter(description = "${test.app95.param.name.description}") String name) {

	}

}
