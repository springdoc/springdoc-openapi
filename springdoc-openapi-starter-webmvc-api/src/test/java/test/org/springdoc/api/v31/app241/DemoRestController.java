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

package test.org.springdoc.api.v31.app241;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoRestController {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Set<String> getDemo(
			@Parameter(required = false, description = "Very important description.")
			@RequestParam(name = "darstellung", required = false) final Optional<String> darstellung) {
		return new HashSet<>();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setDemo(@RequestBody final String str) {
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{vvpId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getDemo(@PathVariable("vvpId") final Long vvpId) {
		return "Test";
	}

	@PatchMapping("/{vvpId}")
	public ResponseEntity<?> patchDemo(@PathVariable("vvpId") final Long idVerfahren,
			@RequestBody() final String jsonPatch,
			final Optional<Boolean> statusKinderAendern) {
		return ResponseEntity.noContent().build();
	}

}
