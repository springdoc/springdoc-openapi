package test.org.springdoc.api.v30.app199;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class CustomExceptionHandler {

	@ResponseStatus(value = INTERNAL_SERVER_ERROR)
	@ExceptionHandler(MyInternalException.class)
	@ResponseBody
	public ErrorDto handleMyInternalException(final MyInternalException ex) {
		return new ErrorDto(ex.getMessage());
	}

	static public class MyInternalException extends Exception {}

}
