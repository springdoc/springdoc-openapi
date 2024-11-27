package test.org.springdoc.api.app174;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Example Controller
 */
@RestController
class ExampleController {

	@PostMapping
	public Test post(){
		return null;
	}

}







