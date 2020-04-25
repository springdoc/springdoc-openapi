package test.org.springdoc.api.app112;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class PersonController {
	private Random ran = new Random();

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
}
