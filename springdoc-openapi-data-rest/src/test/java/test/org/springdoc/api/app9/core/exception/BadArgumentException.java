package test.org.springdoc.api.app9.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadArgumentException extends Exception {

	private static final long serialVersionUID = -4975801683971908022L;

	public BadArgumentException(String message) {
		super(message);
	}

}
