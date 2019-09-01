package test.org.springdoc.api.app19;

import javax.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class HelloController {

	@PostMapping(value = "/persons")
	public String persons(@RequestBody(description = "requestBody description as parameter") String name) {
		return "OK";
	}

	@RequestBody(description = "requestBody description outside")
	@PostMapping(value = "/persons2")
	public String persons2(String name) {
		return "OK";
	}

	@Operation(requestBody = @RequestBody(description = "requestBody inside operation annotation"))
	@PostMapping(value = "/persons3")
	public String persons3(@NotBlank String name) {
		return "OK";
	}

}
