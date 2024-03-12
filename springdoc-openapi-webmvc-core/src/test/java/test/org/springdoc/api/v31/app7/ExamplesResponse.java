package test.org.springdoc.api.v31.app7;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public class ExamplesResponse{
	@Schema(description = "name", requiredMode = REQUIRED, examples = { "name" })
	String name;
	@Schema(description = "subject", requiredMode = REQUIRED, example = "Hello", examples = { "Hello", "World" })
	String subject;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}

