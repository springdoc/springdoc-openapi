package test.org.springdoc.api.v30.app236;


import jakarta.validation.Valid;

public class ClassOne {

	@Valid
	private ClassTwo description;

	public @Valid ClassTwo getDescription() {
		return description;
	}

	public void setDescription(@Valid ClassTwo description) {
		this.description = description;
	}
}