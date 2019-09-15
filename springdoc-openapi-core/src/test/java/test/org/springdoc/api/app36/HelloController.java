package test.org.springdoc.api.app36;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/persons")
	public String persons() {
		return "OK";
	}

}
