/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package test.org.springdoc.api.v30.app110;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * The type Global controller advice.
 */
@ControllerAdvice(assignableTypes = PersonController.class)
class GlobalControllerAdvice //extends ResponseEntityExceptionHandler
{
	/**
	 * Note use base class if you wish to leverage its handling.
	 * Some code will need changing.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

	/**
	 * Problem response entity.
	 *
	 * @param e the e 
	 * @return the response entity
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Problem> problem(final Throwable e) {
		String message = "Problem occured";
		UUID uuid = UUID.randomUUID();
		String logRef = uuid.toString();
		logger.error("logRef=" + logRef, message, e);
		return new ResponseEntity<Problem>(new Problem(logRef, message), HttpStatus.INTERNAL_SERVER_ERROR);
	}


	/**
	 * Handle method argument not valid response entity.
	 *
	 * @param ex the ex 
	 * @return the response entity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
	) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
		List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
		String error;
		for (FieldError fieldError : fieldErrors) {
			error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
			errors.add(error);
		}
		for (ObjectError objectError : globalErrors) {
			error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
			errors.add(error);
		}
		ErrorMessage errorMessage = new ErrorMessage(errors);

		//Object result=ex.getBindingResult();//instead of above can allso pass the more detailed bindingResult
		return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle constraint violated exception response entity.
	 *
	 * @param ex the ex 
	 * @return the response entity
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleConstraintViolatedException(ConstraintViolationException ex
	) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();


		List<String> errors = new ArrayList<>(constraintViolations.size());
		String error;
		for (ConstraintViolation constraintViolation : constraintViolations) {

			error = constraintViolation.getMessage();
			errors.add(error);
		}

		ErrorMessage errorMessage = new ErrorMessage(errors);
		return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle missing servlet request parameter exception response entity.
	 *
	 * @param ex the ex 
	 * @return the response entity
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex
	) {

		List<String> errors = new ArrayList<>();
		String error = ex.getParameterName() + ", " + ex.getMessage();
		errors.add(error);
		ErrorMessage errorMessage = new ErrorMessage(errors);
		return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
	}


	/**
	 * Handle http media type not supported response entity.
	 *
	 * @param ex the ex 
	 * @return the response entity
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public ResponseEntity<ErrorMessage> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex
	) {
		String unsupported = "Unsupported content type: " + ex.getContentType();
		String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
		ErrorMessage errorMessage = new ErrorMessage(unsupported, supported);
		return new ResponseEntity(errorMessage, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	/**
	 * Handle http message not readable response entity.
	 *
	 * @param ex the ex 
	 * @return the response entity
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		Throwable mostSpecificCause = ex.getMostSpecificCause();
		ErrorMessage errorMessage;
		if (mostSpecificCause != null) {
			String exceptionName = mostSpecificCause.getClass().getName();
			String message = mostSpecificCause.getMessage();
			errorMessage = new ErrorMessage(exceptionName, message);
		}
		else {
			errorMessage = new ErrorMessage(ex.getMessage());
		}
		return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
	}

}