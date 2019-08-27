package test.org.springdoc.api.app2.api;

import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionTranslator {

    private final ErrorAttributes errorAttributes;

    public ExceptionTranslator(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> processConstraintViolationException(WebRequest request) {
        request.setAttribute("javax.servlet.error.status_code", HttpStatus.BAD_REQUEST.value(), RequestAttributes.SCOPE_REQUEST);
        return errorAttributes.getErrorAttributes(request, false);
    }
}
