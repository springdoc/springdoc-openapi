package org.springdoc.sample1;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionTranslator {
	/**
	 * logger of type Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionTranslator.class);

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorMessage> handleRunTimeException(RuntimeException e) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }


	private ResponseEntity<ErrorMessage> error(HttpStatus status, Exception e) {
		LOG.error("Exception : ", e);
		return ResponseEntity.status(status).body(new ErrorMessage(UUID.randomUUID().toString(), e.getMessage()));
    }
}