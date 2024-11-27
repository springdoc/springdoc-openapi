package test.org.springdoc.api.app101;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
@RequestMapping("/hello")
class HelloController {

	/**
	 * Hello hello dto.
	 *
	 * @return the hello dto
	 */
	@GetMapping
	@ApiResponse(content = @Content(schema = @Schema(
			description = "${test.app101.operation.hello.response.schema.description}",
			implementation = HelloDTO.class)))
	public HelloDTO hello() {
		return new HelloDTO();
	}

}
