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

package test.org.springdoc.api.v30.app163.rest.util;

import test.org.springdoc.api.v30.app163.exception.NoResultException;
import test.org.springdoc.api.v30.app163.exception.NonUniqueResultException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * REST exception handlers.
 * This javadoc description is ignored by the REST documentation.
 */
@RestControllerAdvice
class RestExceptionHandler {
	/**
	 * REST exception handler for {@code NoResultException}.
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
