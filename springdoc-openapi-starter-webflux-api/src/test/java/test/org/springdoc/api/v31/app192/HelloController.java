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

package test.org.springdoc.api.v31.app192;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping
public class HelloController {

	@Operation(operationId = "getFeaturesJson",
			parameters =
			@Parameter(name = "organizationId" , description = "toto", in = ParameterIn.PATH))
	@GetMapping(value = "/{organizationId}/features", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Feature> getFeaturesAsJson(
			@PathVariable String organizationId) {
		return List.of(/* ... */);
	}

	@Operation(operationId = "getFeaturesNdjson",
			parameters = @Parameter(name = "organizationId", description = "titi",  in = ParameterIn.PATH
					))
	@GetMapping(value = "/{organizationId}/features", produces = "application/x-ndjson")
	public List<Flux> getFeaturesAsNdjson(
			@PathVariable String organizationId) {
		return null;
	}

}
