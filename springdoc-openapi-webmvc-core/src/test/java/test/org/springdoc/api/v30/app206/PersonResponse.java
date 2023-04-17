package test.org.springdoc.api.v30.app206;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class PersonResponse {
	@Schema
	private long id;

	@NotBlank
	@Size(max = 10)
	private String firstName;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String lastName;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private Optional<String> email;

	@Schema(required = true)
	private int age;

	public PersonResponse(long id, String firstName, String lastName, Optional<String> email, int age) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.age = age;
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

	public Optional<String> getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}
}
