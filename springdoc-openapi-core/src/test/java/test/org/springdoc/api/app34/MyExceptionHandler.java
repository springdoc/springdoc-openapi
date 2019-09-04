package test.org.springdoc.api.app34;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
@Hidden
public class MyExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object bad(IllegalArgumentException e) {
		return null;
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	public Object gateway(RuntimeException e) {
		return null;
	}
}