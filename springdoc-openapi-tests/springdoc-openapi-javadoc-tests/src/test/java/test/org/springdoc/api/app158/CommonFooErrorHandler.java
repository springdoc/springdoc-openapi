package test.org.springdoc.api.app158;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Common foo error handler.
 */
class CommonFooErrorHandler {

	/**
	 * On exception error dto.
	 *
	 * @param e the e 
	 * @return the error dto
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorDTO onException(Exception e) {
		return new ErrorDTO("Something wrong has happened");
	}
}
