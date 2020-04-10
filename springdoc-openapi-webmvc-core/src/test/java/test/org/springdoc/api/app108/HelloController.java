package test.org.springdoc.api.app108;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@PostMapping
	public ActionResult<Void> update(String toto) {
		return null;
	}
}

