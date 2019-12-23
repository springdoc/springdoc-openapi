package test.org.springdoc.api.app69.model;

import io.swagger.v3.oas.annotations.media.Schema;
import test.org.springdoc.api.app69.customizer.CustomizedProperty;

public class ApiType {
	@CustomizedProperty
	@Schema(description = "Test description")
	private String someProperty;

	public String getSomeProperty() {
		return someProperty;
	}

	public void setSomeProperty(String someProperty) {
		this.someProperty = someProperty;
	}
}
