package test.org.springdoc.api.app167;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping("/api")
public class HelloController {

	@GetMapping("/sample1")
	public ResponseEntity sample1(@Parameter(name="mySample") String mySample) {
		throw new UnsupportedOperationException("the body is not relevant now");
	}


}
