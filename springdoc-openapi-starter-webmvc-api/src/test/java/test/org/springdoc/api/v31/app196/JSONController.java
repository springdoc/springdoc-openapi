package test.org.springdoc.api.v31.app196;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/json")
@Validated
public class JSONController {

	@GetMapping()
	public ResponseEntity<String> listTemplates() {
		return ResponseEntity.status(HttpStatus.OK).body("Hello World");
	}

}