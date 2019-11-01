package test.org.springdoc.api.app53;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class HelloController {

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello1", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithNoApiResponse() {
		return null;
	}

	@Operation(description = "Some operation")
	@ApiResponse
	@GetMapping(value = "/hello2", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithEmptyApiResponse() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello3", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	List<String> listWithExplicitResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello4", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	HelloDTO1 getDTOWithExplicitResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello5", produces = MediaType.APPLICATION_JSON_VALUE)
	List<String> listWithDefaultResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello6", produces = MediaType.APPLICATION_JSON_VALUE)
	HelloDTO1 getDTOWithDefaultResponseStatus() {
		return null;
	}

	@Operation(description = "Some operation")
	@GetMapping(value = "/hello7", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HelloDTO1> getNestedDTOWithDefaultResponseStatus() {
		return null;
	}

	static class HelloDTO1 {
		private String message;

		public HelloDTO1(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}