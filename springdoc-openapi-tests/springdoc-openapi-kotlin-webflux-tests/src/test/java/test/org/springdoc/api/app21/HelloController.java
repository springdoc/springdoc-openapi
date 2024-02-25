package test.org.springdoc.api.app21;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test")
@RestController
@RequestMapping(value = "/api/v2/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class HelloController {

	@GetMapping("/")
	public void greet(@RequestParam(required = false) @Parameter(required = false) final String name) {
	}

	@GetMapping("/test2")
	public void greet1(@RequestHeader(required = false) @Parameter(required = false) final String name) {
	}
}