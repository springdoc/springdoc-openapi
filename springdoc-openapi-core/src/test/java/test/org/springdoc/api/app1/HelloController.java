package test.org.springdoc.api.app1;

import org.hibernate.validator.constraints.SafeHtml.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(value = "/hello/{numTelco}")
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	@Tag(name = "tea")
	public String index(@PathVariable("numTelco") String numTel, String adresse) {
		return "Greetings from Spring Boot!";
	}

}