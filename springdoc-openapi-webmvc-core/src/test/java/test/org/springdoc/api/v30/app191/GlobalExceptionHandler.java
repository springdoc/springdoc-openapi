package test.org.springdoc.api.v30.app191;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
class GlobalExceptionHandler {

	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	@ExceptionHandler(MyException.class)
	public ResponseEntity<String> handleException(MyException myException) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(myException.getMessage());
	}
}
