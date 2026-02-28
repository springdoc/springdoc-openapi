/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v30.app27;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * The type Advice.
 */
@RestControllerAdvice
class Advice {

	/**
	 * Handle exception foo.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the foo
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Foo handleException(Exception ex, WebRequest request) {
		return new Foo();
	}

	/**
	 * Handle my exception bar.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the bar
	 */
	@ExceptionHandler(MyException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Bar handleMyException(MyException ex, WebRequest request) {
		return new Bar();
	}

	/**
	 * Handle my exception 2 bar.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the bar
	 */
	@Hidden
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Bar handleMyException2(MyException ex, WebRequest request) {
		return new Bar();
	}
}
