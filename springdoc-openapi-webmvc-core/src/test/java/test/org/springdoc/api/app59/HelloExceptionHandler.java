package test.org.springdoc.api.app59;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class HelloExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ApiResponse(responseCode = "500",
			description = "An unknown error occurred"
	)
	protected ResponseEntity<Object> handleException() {
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@ApiResponse(responseCode = "400",
			description = "The request is malformed or information is missing."
	)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}