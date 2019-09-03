package test.org.springdoc.api.app30;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(produces = MediaType.TEXT_PLAIN_VALUE, path = "/test")
	public String echo(@RequestParam(name = "text", defaultValue = "Hello, World!") String text) {
		return text;
	}


}
