package test.org.springdoc.api.app4;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;


@RestController
public class HelloController {


	@Operation(summary = "Parse Resume")
	@PostMapping(value = "/parse-resume", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@ApiResponses({ @ApiResponse(responseCode = "400", description = "Invalid input") })
	public Mono<String> parse(
			@RequestPart(name = "resumeFile") @Parameter(description = "Resume file to be parsed", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)) FilePart resumeFile) {
		return null;
	}
}
