package test.org.springdoc.api.v31.app226;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping
public class HelloController {

	public record Error(String message) {

	}

	@PostMapping("/testBoolean")
	@ApiResponses(value = {
			@ApiResponse(
					useReturnTypeSchema = true,
					responseCode = "200",
					description = "OK",
					content = {
							@Content(
									mediaType = "*/*",
									examples =
									@ExampleObject(
											name = "success",
											value = "..."))
					}
			),
			@ApiResponse(
					responseCode = "400",
					description = "OK",
					content = {
							@Content(
									mediaType = "*/*",
									schema = @Schema(implementation = Error.class))
					}
			)
	})
	public Map<String, String> HelloController() {
		return null;
	}
}
