package test.org.springdoc.api.app164;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiController {

	@GetMapping(value = "/test")
	public SampleResponseClass getInvoices() {
		return null;
	}

}
