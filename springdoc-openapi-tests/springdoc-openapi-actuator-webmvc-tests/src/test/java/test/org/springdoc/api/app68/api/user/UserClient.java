package test.org.springdoc.api.app68.api.user;

import test.org.springdoc.api.app68.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/users", consumes = "application/json")
@Controller
public class UserClient {

	@GetMapping("/{id}")
	public User findById(@PathVariable Integer id) {
		return null;
	}
}
