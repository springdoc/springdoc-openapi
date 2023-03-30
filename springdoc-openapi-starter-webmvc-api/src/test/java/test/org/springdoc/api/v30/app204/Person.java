package test.org.springdoc.api.v30.app204;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Person {
	private long id;

	private String firstName;

	@NotBlank
	@Size(max = 10)
	private String lastName;

	public Person(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
