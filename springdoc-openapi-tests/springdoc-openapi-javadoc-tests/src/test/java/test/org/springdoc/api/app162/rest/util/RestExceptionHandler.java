package test.org.springdoc.api.app162.rest.util;

import test.org.springdoc.api.app162.exception.NoResultException;
import test.org.springdoc.api.app162.exception.NonUniqueResultException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * REST exception handlers.
 *
 * This javadoc description is ignored by the REST documentation.
 */
@RestControllerAdvice
class RestExceptionHandler {
	/**
	 * REST exception handler for {@code NoResultException}.
	 *
	 * This javadoc description is ignored by the REST documentation.
	 *
	 * @return the {@code return} javadoc for the {@code #handleNotFoundException(NoResultException)} method
	 */
	@ExceptionHandler(NoResultException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleNotFoundException(NoResultException exception) {
		return new ResponseEntity<>("No result for the arguments.", HttpStatus.NOT_FOUND);
	}

	/**
	 * REST exception handler for {@code NonUniqueResultException}.
	 *
	 * This javadoc description is ignored by the REST documentation.
	 *
	 * @return the {@code return} javadoc for the {@code #handleNonUniqueResultException(NonUniqueResultException)} method
	 */
	@ExceptionHandler(NonUniqueResultException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleNonUniqueResultException(NonUniqueResultException exception) {
		return new ResponseEntity<>("No unique result found for the arguments.", HttpStatus.BAD_REQUEST);
	}

}
