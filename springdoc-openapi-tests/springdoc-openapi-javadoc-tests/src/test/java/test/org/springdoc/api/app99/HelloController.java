package test.org.springdoc.api.app99;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
@RequestMapping("/persons")
class HelloController {

	/**
	 * Persons.
	 */
	@GetMapping
	@ApiResponses({
			@ApiResponse(responseCode = "202", description = "${test.app99.operation.persons.response.202.description}")
	})
	public void persons() {

	}

}
