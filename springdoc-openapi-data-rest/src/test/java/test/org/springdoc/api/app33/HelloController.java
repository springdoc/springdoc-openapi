package test.org.springdoc.api.app33;

import org.springdoc.api.annotations.ParameterObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {


	@GetMapping("/items/nested")
	public void showNestedItem(@ParameterObject ExampleSort exampleSort) {
	}

}
