package test.org.springdoc.api.app179;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
	@GetMapping("/test/{objId}")
	String test(@MyIdPathVariable MyObj obj) {
		return obj.getContent();
	}
}