package test.org.springdoc.api.v30.app208;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */

@RestController
@RequestMapping("/test1")
public class HelloController {

	@GetMapping
	public String test1(RequestObject object) {
		return null;
	}

	@PostMapping
	public String test2(@RequestBody RequestObject obj) {
		return null;
	}
}