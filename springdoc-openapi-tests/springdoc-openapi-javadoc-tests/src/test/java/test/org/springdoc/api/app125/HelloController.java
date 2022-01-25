package test.org.springdoc.api.app125;

import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 * @author bnasslahsen
 */
@RestController
public class HelloController {

	/**
	 * Gets all pets.
	 *
	 * @param toto the toto 
	 * @return the all pets
	 */
	@GetMapping(value = "/search", produces = { "application/xml", "application/json" })
	public DeprecatedEntity getAllPets(@NotNull String toto) {
		return null;
	}

}
