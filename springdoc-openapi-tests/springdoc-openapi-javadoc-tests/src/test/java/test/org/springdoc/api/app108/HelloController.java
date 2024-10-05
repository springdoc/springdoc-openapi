package test.org.springdoc.api.app108;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Update action result.
	 *
	 * @param toto the toto 
	 * @return the action result
	 */
	@PostMapping
	public ActionResult<Void> update(String toto) {
		return null;
	}
}

