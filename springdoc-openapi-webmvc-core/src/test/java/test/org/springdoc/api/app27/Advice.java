package test.org.springdoc.api.app27;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
public class Advice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Foo handleException(Exception ex, WebRequest request) {
        return new Foo();
    }

    @ExceptionHandler(MyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Bar handleMyException(MyException ex, WebRequest request) {
        return new Bar();
    }

	@Hidden
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Bar handleMyException2(MyException ex, WebRequest request) {
		return new Bar();
	}
}
