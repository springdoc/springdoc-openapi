package test.org.springdoc.api.v31.app230;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author edudar
 */
@RestController
@RequestMapping
public class App230Controller {

	@PostMapping("/")
	public String swaggerTest(@App230RequestBody MyRequest myRequest) {
		return null;
	}

	public record MyRequest(int id) {
	}

}
