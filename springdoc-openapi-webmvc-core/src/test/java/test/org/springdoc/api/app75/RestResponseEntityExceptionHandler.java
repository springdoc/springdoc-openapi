package test.org.springdoc.api.app75;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<List<Object>> badRequest(HttpServletRequest req, Exception exception) {
        return null;
    }
}