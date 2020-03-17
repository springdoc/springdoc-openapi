package test.org.springdoc.api.app99;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/persons")
	public void persons() {

	}

}