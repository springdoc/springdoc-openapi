package test.org.springdoc.api.app170;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a Dog class.")
public class Dog implements Animal {

	private String name;

	private Integer age;

	public Dog(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
