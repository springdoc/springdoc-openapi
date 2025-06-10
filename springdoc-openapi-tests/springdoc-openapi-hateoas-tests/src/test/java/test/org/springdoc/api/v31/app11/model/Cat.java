package test.org.springdoc.api.v31.app11.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a Cat class.")
public class Cat {

	@JsonUnwrapped
	@Schema(description = "The name.", nullable = true)
	private String name;

	public Cat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
