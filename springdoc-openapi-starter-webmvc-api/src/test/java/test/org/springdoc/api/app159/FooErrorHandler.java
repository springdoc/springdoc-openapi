package test.org.springdoc.api.app159;

import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = HelloController.class)
public class FooErrorHandler {

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@JsonView(Views.View1.class)
	public ResponseEntity<FooBean> storeAssignmentPublishingError(Exception e) {
		return new ResponseEntity<>(new FooBean("INTERNAL_SERVER_ERROR",500), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@JsonView(Views.View2.class)
	public ResponseEntity<FooBean> storeAssignmentPublishingError(CustomException e) {
		return new ResponseEntity<>(new FooBean("BAD Request",400), HttpStatus.BAD_REQUEST);
	}
}
