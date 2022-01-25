package test.org.springdoc.api.app169;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@PostMapping("/echo")
	public String echo(String content) {
		return content;
	}
}
