/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app112;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@RestController
@Validated
public class PersonController {
	private final Random ran = new Random();

	@RequestMapping(path = "/person", method = RequestMethod.POST)
	public Person person(@Valid @RequestBody Person person) {

		int nxt = ran.nextInt(10);
		if (nxt >= 5) {
			throw new RuntimeException("Breaking logic");
		}
		return person;
	}

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

	@RequestMapping(path = "/persons", method = RequestMethod.GET)
	public List<Person> findPersons(
			@RequestParam(name = "setsOfShoes") @Range(min = 1, max = 4) int setsOfShoes,
			@RequestParam(name = "height") @Range(max = 200) int height,
			@RequestParam(name = "age") @Range(min = 2) int age,
			@RequestParam(name = "oneToTen") @ComposedInterfaceWithStaticDefinitions int oneToTen
	) {
		return List.of();

	}

	@Min(1)
	@Max(10)
	@Retention(RUNTIME)
	public @interface ComposedInterfaceWithStaticDefinitions {
	}
}
