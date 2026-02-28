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

package test.org.springdoc.api.v30.app112;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Person controller.
 */
@RestController
@Validated
class PersonController {
	/**
	 * The Ran.
	 */
	private Random ran = new Random();

	/**
	 * Person person.
	 *
	 * @param person the person
	 * @return the person
	 */
	@RequestMapping(path = "/person", method = RequestMethod.POST)
	public Person person(@Valid @RequestBody Person person) {

		int nxt = ran.nextInt(10);
		if (nxt >= 5) {
			throw new RuntimeException("Breaking logic");
		}
		return person;
	}

	/**
	 * Find by last name list.
	 *
	 * @param lastName the last name
	 * @return the list
	 */
	@RequestMapping(path = "/personByLastName", method = RequestMethod.GET)
	public List<Person> findByLastName(@RequestParam(name = "lastName", required = true) @NotNull
	@NotBlank
	@Size(max = 10) String lastName) {
		List<Person> hardCoded = new ArrayList<>();
		Person person = new Person();
		person.setAge(20);
		person.setCreditCardNumber("4111111111111111");
		person.setEmail("abc@abc.com");
		person.setEmail1("abc1@abc.com");
		person.setFirstName("Somefirstname");
		person.setLastName(lastName);
		person.setId(1);
		hardCoded.add(person);
		return hardCoded;

	}
}
