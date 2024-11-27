package test.org.springdoc.api.app116;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The type Foo error handler.
 */
@ControllerAdvice(assignableTypes = HelloController.class)
class FooErrorHandler {

	/**
	 * Store assignment publishing error response entity.
	 *
	 * @param e the e 
	 * @return the response entity
	 */
	@ExceptionHandler
	public ResponseEntity<String> storeAssignmentPublishingError(Exception e) {
		return new ResponseEntity<>("foo", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
