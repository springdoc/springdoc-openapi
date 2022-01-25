package test.org.springdoc.api.app166;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component("1") // bean name override to simulate order in HashMap
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus
	public GlobalErrorResponseDto processException() {
		return new GlobalErrorResponseDto("global");
	}
}
