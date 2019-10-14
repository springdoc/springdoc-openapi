package test.org.springdoc.api.app47;

import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class HelloController {
	@GetMapping(path = "/documents/{locale}")
	public ResponseEntity<String> getDocumentsWithLocale(
			@Parameter(schema = @Schema(type = "string")) @PathVariable("locale") Locale locale) {
		return null;
	}
}
