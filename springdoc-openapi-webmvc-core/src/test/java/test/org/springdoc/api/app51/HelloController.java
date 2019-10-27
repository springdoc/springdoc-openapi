package test.org.springdoc.api.app51;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class HelloController {

	@Operation(parameters = {
			@Parameter(in = ParameterIn.HEADER, name = "test_header", required = true, schema = @Schema(type = "string", example = "rherherherherh")) })
	@GetMapping("/test1")
	public String test1() {
		return "test";
	}

	@Operation(parameters = {
			@Parameter(in = ParameterIn.HEADER, name = "test_header", required = true, schema = @Schema(type = "string", example = "rherherherherh")) })
	@GetMapping("/test2")
	public String test2(@RequestParam(name = "param1") String param1) {
		return "test";
	}

	@Operation(parameters = {
			@Parameter(in = ParameterIn.HEADER, name = "test_header", required = true, schema = @Schema(type = "string", example = "rherherherherh")),
			@Parameter(description = "desc1", in = ParameterIn.QUERY, name = "param1", required = true, schema = @Schema(type = "string", example = "something")) })
	@GetMapping("/test3")
	public String test3(
			@RequestParam(name = "param1") @Parameter(description = "desc2", in = ParameterIn.QUERY) String param1) {
		return "test";
	}

}