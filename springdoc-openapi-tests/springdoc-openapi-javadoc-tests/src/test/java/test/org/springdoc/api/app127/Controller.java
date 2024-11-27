package test.org.springdoc.api.app127;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Controller.
 */
@RestController
class Controller {

	/**
	 * Bug umbrella.
	 *
	 * @return the umbrella
	 */
	@Operation(summary = "Test Bug", responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(
							schema = @Schema(implementation = Umbrella.class),
							examples = @ExampleObject(ref = "#/components/examples/umbrellaExample", name = "Example with weird YAML tag")
					)
			)
	})
	@GetMapping(value = "/bug", produces = MediaType.APPLICATION_JSON_VALUE)
	public Umbrella bug() {
		return new Umbrella(new ConcreteObjectA("a", "b"));
	}
}
