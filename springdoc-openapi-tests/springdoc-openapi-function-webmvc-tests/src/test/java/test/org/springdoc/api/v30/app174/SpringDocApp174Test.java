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

package test.org.springdoc.api.v30.app174;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import test.org.springdoc.api.v30.AbstractSpringDocTest;
import test.org.springdoc.api.v30.app175.PersonDTO;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;

public class SpringDocApp174Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {
		@Bean
		public Function<String, String> reverseString() {
			return value -> new StringBuilder(value).reverse().toString();
		}

		@Bean
		public Function<String, String> uppercase() {
			return String::toUpperCase;
		}

		@Bean
		@RouterOperations({
				@RouterOperation(method = RequestMethod.GET, operation = @Operation(description = "Say hello GET", operationId = "lowercaseGET", tags = "positions",
						responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))))),
				@RouterOperation(method = RequestMethod.POST, operation = @Operation(description = "Say hello POST", operationId = "lowercasePOST", tags = "positions",
						responses = @ApiResponse(responseCode = "200", description = "new desc", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class))))))
		})
		public Function<List<String>, List<String>> lowercase() {
			return list -> list.stream().map(String::toLowerCase).toList();
		}

		@Bean(name = "titi")
		@RouterOperation(operation = @Operation(description = "Say hello By Id", operationId = "hellome", tags = "persons",
				responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class)))))
		public Supplier<PersonDTO> helloSupplier() {
			return PersonDTO::new;
		}

		@Bean
		public Consumer<PersonDTO> helloConsumer() {
			return PersonDTO::getFirstName;
		}

		@Bean
		public Supplier<List<String>> words() {
			return () -> Arrays.asList("foo", "bar");
		}

	}

}