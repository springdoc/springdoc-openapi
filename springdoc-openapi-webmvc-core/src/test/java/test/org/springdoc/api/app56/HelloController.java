package test.org.springdoc.api.app56;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/persons")
	public String persons() {
		return "OK";
	}

}
