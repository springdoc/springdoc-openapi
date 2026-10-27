/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.org.springdoc.api.v30.app283;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Operations are processed in this order: PUT /cats, POST /pets, POST /cats, GET /cats/count.
 */
@RestController
public class HelloController implements CatApi {

	@JsonSubTypes(@JsonSubTypes.Type(Cat.class))
	public abstract static class Animal {
		public String name;
	}

	public static class Cat extends Animal {
		public Boolean indoor;
	}

	/** No annotations on the implementation: same cache key in 3.1.1 and 3.2.0. */
	@Override
	public void updateCat(Cat cat) {
	}

	/** Repeats @Valid, which 3.2.0 also picks up as type-use annotation: new cache key. */
	@Override
	public void createCat(@Valid Cat cat) {
	}

	/** Resolves the parent, which turns Cat into allOf [Animal, ...]. */
	@PostMapping("/pets")
	public void createPet(@RequestBody Animal animal) {
	}

	/** Any later schema extraction merges the shared context back into the components. */
	@GetMapping("/cats/count")
	public String countCats() {
		return "0";
	}
}

interface CatApi {

	@PutMapping("/cats")
	void updateCat(@Valid @RequestBody HelloController.Cat cat);

	@PostMapping("/cats")
	@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = HelloController.Cat.class)))
	void createCat(@Valid @RequestBody HelloController.Cat cat);
}
