package test.org.springdoc.api.app38;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class HelloController {

	@RequestMapping(value = "/npe_error", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getModelResource() {
		return new ResponseEntity<>(new byte[0], HttpStatus.OK);
	}

}