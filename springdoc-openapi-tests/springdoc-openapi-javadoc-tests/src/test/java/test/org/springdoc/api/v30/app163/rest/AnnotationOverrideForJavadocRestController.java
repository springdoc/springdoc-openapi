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

package test.org.springdoc.api.v30.app163.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import test.org.springdoc.api.v30.app163.exception.NoResultException;
import test.org.springdoc.api.v30.app163.exception.NonUniqueResultException;
import test.org.springdoc.api.v30.app163.rest.dto.AnnotationOverrideForJavadocRestDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the {@code AnnotationOverrideForJavadocRestController} class javadoc.
 */
@Tag(
		name = "annotation-override",
		description = "Description for the tag."
)
@RestController
@RequestMapping("/annotation-override")
class AnnotationOverrideForJavadocRestController {
	/**
	 * This is the update method's javadoc.
	 * The method's signature: {@code #update(String, AnnotationOverrideForJavadocRestDto)}
	 *
	 * @param guid the {@code @param input} javadoc for the {@code #update(String, AnnotationOverrideForJavadocRestDto)} method
	 * @return the {@code @return} javadoc for the {@code #update(String, AnnotationOverrideForJavadocRestDto)} method
	 */
	@Operation(
			summary = "Summary for #update(String, AnnotationOverrideForJavadocRestDto)"
	)
	@PutMapping(path = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<AnnotationOverrideForJavadocRestDto> update(@PathVariable String guid, @RequestBody AnnotationOverrideForJavadocRestDto input) throws NoResultException {
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * This is the create method's javadoc.
	 * The method's signature: {@code #create(AnnotationOverrideForJavadocRestDto)}
	 *
	 * @param input the {@code @param input} javadoc for the {@code #create(AnnotationOverrideForJavadocRestDto)} method
	 * @return the {@code @return} javadoc for the {@code #create(AnnotationOverrideForJavadocRestDto)} method
	 */
	@Operation(
			summary = "Summary for #create(AnnotationOverrideForJavadocRestDto)",
			description = "Description for #create(AnnotationOverrideForJavadocRestDto)",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Request body for  #create(AnnotationOverrideForJavadocRestDto)"
			),
			responses = {
					@ApiResponse(
							description = "API Response 201 for #create(AnnotationOverrideForJavadocRestDto)"
					)
			}
	)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<AnnotationOverrideForJavadocRestDto> create(@RequestBody AnnotationOverrideForJavadocRestDto input) {
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * This is the findStartsBy method's javadoc.
	 * The method's signature: {@code #findStartsBy(String)}
	 *
	 * @param prefix the {@code @param prefix} javadoc for the {@code #findStartsBy(String)} method
	 * @return the {@code @return} javadoc for the {@code #findStartsBy(String)} method
	 * @throws NoResultException        the {@code @throws NoResultException} javadoc for the {@code #findStartsBy(String)} method
	 * @throws NonUniqueResultException the {@code @throws NonUniqueResultException} javadoc for the {@code #findStartsBy(String)} method
	 */
	@Operation(
			parameters = {
					@Parameter(
							name = "prefix",
							description = "Parameter prefix"
					)
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "API Response 200 for #findStartsBy(prefix)"
					),
					@ApiResponse(
							responseCode = "400",
							description = "API Response 400 for #findStartsBy(prefix)"
					)
			}
	)
	@GetMapping(path = "startsBy/{prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AnnotationOverrideForJavadocRestDto> findStartsBy(@PathVariable String prefix) throws NoResultException, NonUniqueResultException {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
