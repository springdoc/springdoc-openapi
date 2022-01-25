/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.app150;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
public class HelloController {

	@GetMapping("/test/")
	@ApiResponse(responseCode = "204", description = "No content")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void test(@RequestParam(defaultValue = "1") Integer toto) {

	}

	@GetMapping("/test1")
	@ApiResponse(responseCode = "204", description = "No content")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void test1(@RequestParam @Parameter(schema = @Schema(defaultValue ="false", type = "boolean")) boolean toto) {

	}

	@GetMapping("/test3")
	@ApiResponse(responseCode = "204", description = "No content")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void test3(@RequestParam(defaultValue = "users,123") List<String> toto) {

	}

	@GetMapping("/test4")
	@ApiResponse(responseCode = "204", description = "No content")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void test4(@DateTimeFormat(iso = DATE) @RequestParam(defaultValue = "2021-03-08") LocalDate localDate) {

	}

}
