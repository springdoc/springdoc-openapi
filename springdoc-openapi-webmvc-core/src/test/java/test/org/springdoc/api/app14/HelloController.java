package test.org.springdoc.api.app14;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(value = "/persons")
	public void persons(@Valid @NotBlank String name) {

	}
	
}
