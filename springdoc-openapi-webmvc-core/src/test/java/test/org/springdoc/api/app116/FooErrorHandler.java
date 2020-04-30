package test.org.springdoc.api.app116;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = HelloController.class)
public class FooErrorHandler {

	@ExceptionHandler
	public ResponseEntity<String> storeAssignmentPublishingError(Exception e) {
		return new ResponseEntity<>("foo", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
