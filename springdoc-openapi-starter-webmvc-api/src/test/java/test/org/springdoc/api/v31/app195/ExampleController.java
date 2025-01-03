package test.org.springdoc.api.v31.app195;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping("/example")
public class ExampleController {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiResponse(responseCode = "500", description = "ExceptionHandler in example")
	public String customControllerException() {
		return "example";
	}

	@GetMapping("/500")
	@Operation(
			tags = { "example" },
			summary = "Example method",
			description = "This method is an example"
	)
	public void test() {
		throw new RuntimeException();
	}
}