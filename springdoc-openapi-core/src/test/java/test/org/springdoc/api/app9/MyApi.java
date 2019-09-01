package test.org.springdoc.api.app9;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@RequestMapping("/myapi")
public interface MyApi {

	@Operation(description = "Annotations from interfaces test")
	@GetMapping
	String get(
			@Parameter(hidden = true, in = ParameterIn.HEADER, name = HttpHeaders.ACCEPT_LANGUAGE) @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String language);
}