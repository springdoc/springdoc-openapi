package test.org.springdoc.api.app173;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The Example object
 */
@Schema
class Example {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
