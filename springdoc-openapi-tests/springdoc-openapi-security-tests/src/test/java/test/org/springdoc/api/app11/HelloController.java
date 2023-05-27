package test.org.springdoc.api.app11;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author yuta.saito
 */
@RestController
@RequestMapping("/api")
public class HelloController {
	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}
}
