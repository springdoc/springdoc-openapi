package test.org.springdoc.api.v31.app206;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequiredModeController {
	@GetMapping
	public PersonResponse index(@ParameterObject PersonRequest request) {
		return null;
	}
}
