package test.org.springdoc.api.app6.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/fax")
@Tag(name = "Fax stuff", description = "For managing fax machines.")
public class MyController {

	@Operation(summary = "Get information about currently existing fax machines")
	@ApiResponse(responseCode = "200", description = "list of existing fax machines")
	@GetMapping("list")
	public List<String> getFaxList(@RequestParam(name = "vendorName", required = false)
	@Parameter(description = "vendor name to restrict the list") String vendorFilter) {

		return null;
	}

}
