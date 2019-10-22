package test.org.springdoc.api.app51;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "${tag.name}", description = "${tag.description}")
public class HelloController {

	@Operation(description = "${operation.description}", responses = { @ApiResponse(responseCode = "401") })
	@ApiResponse(responseCode = "401", content = @Content())
	@GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> list(@RequestAttribute @Parameter(description = "${parameter.description}", name = "${parameter.name}") String test) {
		return null;
	}

}
