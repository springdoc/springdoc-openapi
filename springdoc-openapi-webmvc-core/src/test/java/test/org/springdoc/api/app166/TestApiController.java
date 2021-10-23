package test.org.springdoc.api.app166;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component("0") // bean name override to simulate order in HashMap
public class TestApiController {

	@GetMapping(value = "/test")
	public String throwError() {
		throw new IllegalArgumentException();
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus
	public LocalErrorResponseDto processException() {
		return new LocalErrorResponseDto("local");
	}
}
