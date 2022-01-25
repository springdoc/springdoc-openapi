package test.org.springdoc.api.app28;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping("/api")
public class HelloController {


	@GetMapping("/items/nested")
	public void showNestedItem(@ParameterObject ExamplePageable examplePageable) {
	}

}
