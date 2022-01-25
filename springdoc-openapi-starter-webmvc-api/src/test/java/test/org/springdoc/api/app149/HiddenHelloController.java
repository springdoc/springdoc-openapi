package test.org.springdoc.api.app149;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping("/api")
public class HiddenHelloController {

	@Operation(description = "I want here some custom config")
	@GetMapping("/{entity}/{id}")
	public ResponseEntity getEntity() {
		throw new UnsupportedOperationException("the body is not relevant now");
	}
}
