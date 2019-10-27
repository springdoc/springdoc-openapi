package test.org.springdoc.api.app13;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(value = "/persons")
	public String persons(final PersonDTO dto) {
		return "OK";
	}
	
}
