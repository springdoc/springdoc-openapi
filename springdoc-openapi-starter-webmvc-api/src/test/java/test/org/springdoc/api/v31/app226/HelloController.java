package test.org.springdoc.api.v31.app226;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping
public class HelloController {

	@PostMapping("/testBoolean")
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
									value ="..."))
			}
	)
	public Map<String, String> HelloController() {
		return null;
	}
}
