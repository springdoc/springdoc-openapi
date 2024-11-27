package test.org.springdoc.api.app116;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
@RequestMapping("/api")
@OpenAPIDefinition(info = @Info(title = "API Examples", version = "1.0"), tags = @Tag(name = "Operations"))
class HelloController {

	/**
	 * Create string.
	 *
	 * @param foo the foo 
	 * @return the string
	 */
	@PostMapping("/foo")
	public String create(@RequestBody String foo) {
		return "foo";
	}
}


