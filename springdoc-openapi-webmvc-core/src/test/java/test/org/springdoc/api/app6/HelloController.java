package test.org.springdoc.api.app6;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class HelloController {

	@Operation(summary = "Get Something by key", responses = {
			@ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {
					String.class, Integer.class }), examples = {
							@ExampleObject(name = "The String example", value = "urgheiurgheirghieurg"),
							@ExampleObject(name = "The Integer example", value = "311414") })),
			@ApiResponse(responseCode = "404", description = "Thing not found"),
			@ApiResponse(responseCode = "401", description = "Authentication Failure") })
	@GetMapping(value = "/hello")
	ResponseEntity<Void> sayHello() {
		return null;
	}
}
