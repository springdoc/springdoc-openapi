package test.org.springdoc.api.app37;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController("/api")
public class HelloController {

	@PostMapping(path = "/bar/baz", consumes = "application/x.a+json", produces = MediaType.TEXT_PLAIN_VALUE)
	public Foo process(@RequestBody Foo a) {
		return a;
	}

	@PostMapping(path = "/bar/baz", consumes = "application/x.b+json", produces = MediaType.TEXT_PLAIN_VALUE)
	public Bar process(@RequestBody Bar b) {
		return b;
	}

	@PostMapping(path = "/bar/baz", consumes = "application/x.c+json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Car process(@RequestBody Car c) {
		return c;
	}

	@PostMapping(value = "/pets", consumes = "application/json")
	public ResponseEntity<Void> petsPost(@Valid @RequestBody Pet pet) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@PostMapping(value = "/pets", consumes = "text/plain")
	public ResponseEntity<Void> petsPost(@Valid @RequestBody String pet) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

}