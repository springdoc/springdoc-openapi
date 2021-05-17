package test.org.springdoc.api.app158;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CommonFooErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO onException(Exception e) {
        return new ErrorDTO("Something wrong has happened");
    }
}
