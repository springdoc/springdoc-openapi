package test.org.springdoc.api.app91;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type Greeting controller.
 */
@RestController
@Tag(name = "Demo", description = "The Demo API")
class GreetingController {

	/**
	 * Say hello response entity.
	 *
	 * @return the response entity
	 */
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "This API will return a random greeting.")
	public ResponseEntity<Greeting> sayHello() {
		return ResponseEntity.ok(new Greeting(RandomStringUtils.randomAlphanumeric(10)));
	}

	/**
	 * Say hello 2 response entity.
	 *
	 * @return the response entity
	 */
	@GetMapping("/test")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "item created"),
			@ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
			@ApiResponse(responseCode = "409", description = "an existing item already exists") })
	public ResponseEntity<Greeting> sayHello2() {
		return ResponseEntity.ok(new Greeting(RandomStringUtils.randomAlphanumeric(10)));
	}

}
