package test.org.springdoc.api.v31.app231;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class SuperClass {
	@Size(min = 1, max = 30)
	@Schema(description = "Description from the super class")
	private String name;

	public SuperClass(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
