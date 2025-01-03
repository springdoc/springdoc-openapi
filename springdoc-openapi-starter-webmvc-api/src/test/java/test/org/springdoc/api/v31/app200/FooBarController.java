package test.org.springdoc.api.v31.app200;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooBarController {
	@GetMapping(value = "/example/{fooBar}")
	public String getFooBar(@PathVariable FooBar fooBar) {
		return fooBar.name();
	}
}