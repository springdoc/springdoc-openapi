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

package test.org.springdoc.api.v31.app162.rest;

import java.util.List;

import test.org.springdoc.api.v31.app162.exception.NoResultException;
import test.org.springdoc.api.v31.app162.exception.NonUniqueResultException;
import test.org.springdoc.api.v31.app162.rest.dto.JavadocOnlyRestDto;

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
 * This is the {@code JavadocOnlyRestController} class javadoc.
 */
@RestController
@RequestMapping("/javadoc-only")
class JavadocOnlyRestController {
	/**
	 * This is the create method's javadoc.
	 * The method's signature: {@code #create(JavadocOnlyRestDto)}
	 *
	 * @param input the {@code @param input} javadoc for the {@code #create(JavadocOnlyRestDto)} method
	 * @return the {@code @return} javadoc for the {@code #create(JavadocOnlyRestDto)} method
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<JavadocOnlyRestDto> create(@RequestBody JavadocOnlyRestDto input) {
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * This is the update method's javadoc.
	 * The method's signature: {@code #update(String, JavadocOnlyRestDto)}
	 *
	 * @param guid the {@code @param input} javadoc for the {@code #update(String, JavadocOnlyRestDto)} method
	 * @return the {@code @return} javadoc for the {@code #update(String, JavadocOnlyRestDto)} method
	 */
	@PutMapping(path = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<JavadocOnlyRestDto> update(@PathVariable String guid, @RequestBody JavadocOnlyRestDto input) throws NoResultException {
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * This is the list method's javadoc.
	 * The method's signature: {@code #list()}
	 *
	 * @return the {@code @return} javadoc for the {@code #list()} method
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JavadocOnlyRestDto>> list() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * This is the find method's javadoc.
	 * The method's signature: {@code #find(String)}
	 *
	 * @param guid the {@code @param guid} javadoc for the {@code #find(String)} method
	 * @return the {@code @return} javadoc for the {@code #find(String)} method
	 * @throws NoResultException the {@code @throws NoResultException} javadoc for the {@code #find(String)} method
	 */
	@GetMapping(path = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JavadocOnlyRestDto> find(@PathVariable String guid) throws NoResultException {
		return new ResponseEntity<>(HttpStatus.OK);
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
	@GetMapping(path = "startsBy/{prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JavadocOnlyRestDto> findStartsBy(@PathVariable String prefix) throws NoResultException, NonUniqueResultException {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
