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

package test.org.springdoc.api.v31.app159;

import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Foo error handler.
 */
@ControllerAdvice(assignableTypes = HelloController.class)
class FooErrorHandler {

	/**
	 * Store assignment publishing error response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@JsonView(Views.View1.class)
	public ResponseEntity<FooBean> storeAssignmentPublishingError(Exception e) {
		return new ResponseEntity<>(new FooBean("INTERNAL_SERVER_ERROR", 500), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Store assignment publishing error response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@JsonView(Views.View2.class)
	public ResponseEntity<FooBean> storeAssignmentPublishingError(CustomException e) {
		return new ResponseEntity<>(new FooBean("BAD Request", 400), HttpStatus.BAD_REQUEST);
	}
}
