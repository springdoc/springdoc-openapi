package test.org.springdoc.api.app183;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
@Tag(name = "NetworkServices", description = "the NetworkServices API")
public class HelloController {




	@GetMapping("/{userId}")
	public User doSomething(@PathVariable("userId") User user) {
		return new User(user.getId(), "tototot");
	}


}