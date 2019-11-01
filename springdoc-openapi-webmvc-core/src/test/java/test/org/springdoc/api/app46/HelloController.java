package test.org.springdoc.api.app46;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class HelloController {

	@GetMapping("/persons/{subscriptionId}")
	@Operation(operationId = "operationId", summary = "Operation Summary", description = "Operation Description", tags = {
			"Example Tag" }, externalDocs = @ExternalDocumentation(description = "External documentation description", url = "http://url.com"), parameters = {
					@Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true, description = "parameter description", allowEmptyValue = true, allowReserved = true, schema = @Schema(type = "string", format = "uuid", description = "the generated UUID", accessMode = Schema.AccessMode.READ_ONLY)) }, responses = {
							@ApiResponse(responseCode = "200", description = "voila!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))) })
	public String persons(String subscriptionId) {
		return "OK";
	}

}