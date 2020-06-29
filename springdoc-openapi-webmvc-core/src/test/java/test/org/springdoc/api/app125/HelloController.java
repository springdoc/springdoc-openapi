package test.org.springdoc.api.app125;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
public class HelloController {

	@GetMapping(value = "/search", produces = { "application/xml", "application/json" })
	public DeprecatedEntity getAllPets(@NotNull String toto) {
		return null;
	}

}
