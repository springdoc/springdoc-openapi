package test.org.springdoc.api.app11;

/**
 * @author bnasslahsen
 */

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
	@PostMapping
	@ApiResponse(responseCode = "200", description = "Random endpoint.")
	@ResponseStatus(HttpStatus.OK)
	public void testingMethod(@RequestBody TestRequest testRequest) {
		System.out.println("Method was run!");
	}
}