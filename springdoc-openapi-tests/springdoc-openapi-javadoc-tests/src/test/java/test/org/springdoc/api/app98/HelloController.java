package test.org.springdoc.api.app98;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Persons.
	 *
	 * @param name the name
	 */
	@GetMapping("/persons")
	public void persons(@IgnoredAnnotationParameter String name) {

	}

}