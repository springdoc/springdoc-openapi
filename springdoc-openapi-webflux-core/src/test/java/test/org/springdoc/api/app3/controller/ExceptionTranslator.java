package test.org.springdoc.api.app3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import test.org.springdoc.api.app3.exception.TweetConflictException;
import test.org.springdoc.api.app3.exception.TweetNotFoundException;
import test.org.springdoc.api.app3.payload.ErrorResponse;

@RestControllerAdvice
public class ExceptionTranslator {


    @SuppressWarnings("rawtypes")
    @ExceptionHandler(TweetConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity handleDuplicateKeyException(TweetConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("A Tweet with the same text already exists"));
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(TweetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleTweetNotFoundException(TweetNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}
