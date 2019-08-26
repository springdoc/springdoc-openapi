package test.org.springdoc.api.app1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@PostMapping(value = "/values/data")
	TrackerData list(TrackerData toto) {
		return toto;

	}

}
