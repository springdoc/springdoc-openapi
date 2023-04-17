package test.org.springdoc.api.v30.app206;

import org.springdoc.api.annotations.ParameterObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequiredModeController {
	@GetMapping
	public PersonResponse index(@ParameterObject PersonRequest request) {
		return null;
	}
}
