package test.org.springdoc.api.v30.app205;

/**
 *
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


public class OrderDemo {

	@RestController
	public static class MyController {

		@GetMapping("/test/{*param}")
		public String testingMethod(@PathVariable("param") String param) {
			return "foo";
		}
	}

}