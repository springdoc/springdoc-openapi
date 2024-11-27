package test.org.springdoc.api.app164.rest;

import test.org.springdoc.api.app164.exception.NoResultException;
import test.org.springdoc.api.app164.exception.NonUniqueResultException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the {@code JavadocOnlyRestController} class javadoc.
 */
@RestController
@RequestMapping("/no-generic-override")
class NoGenericOverrideRestController {
	/**
	 * This is the create method's javadoc.
	 * The method's signature: {@code #create(JavadocOnlyRestDto)}
	 *
	 * @param input the {@code @param input} javadoc for the {@code #create(JavadocOnlyRestDto)} method
	 * @return the {@code @return} javadoc for the {@code #create(JavadocOnlyRestDto)} method
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> create(@RequestBody String input) {
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * This is the findStartsBy method's javadoc.
	 * The method's signature: {@code #findStartsBy(String)}
	 *
	 * @param prefix the {@code @param prefix} javadoc for the {@code #findStartsBy(String)} method
	 * @return the {@code @return} javadoc for the {@code #findStartsBy(String)} method
	 * @throws NoResultException the {@code @throws NoResultException} javadoc for the {@code #findStartsBy(String)} method
	 * @throws NonUniqueResultException the {@code @throws NonUniqueResultException} javadoc for the {@code #findStartsBy(String)} method
	 */
	@GetMapping(path = "startsBy/{prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> findStartsBy(@PathVariable String prefix) throws NoResultException, NonUniqueResultException {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
