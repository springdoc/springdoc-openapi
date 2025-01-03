package test.org.springdoc.api.v31.app193;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class BasicController {

	@GetMapping("/test")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "get", description = "Provides a list of books.")
	public Knowledge get() {
		return new Books(new Book("Introduction to algorithms"));
	}

	@GetMapping("/test1")
	@Operation(summary = "get1", description = "Provides an animal.")
	public Animal get1() {

		return new Dog("Foo", 12);
	}
}
