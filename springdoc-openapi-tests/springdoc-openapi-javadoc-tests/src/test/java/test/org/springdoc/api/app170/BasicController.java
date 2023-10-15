package test.org.springdoc.api.app170;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class BasicController {

	@GetMapping("/test1")
	@Operation(summary = "get1", description = "Provides an animal.")
	public Animal get1() {

		return new Dog("Foo", 12);
	}
}
