package test.org.springdoc.api.v31.app8;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "user info")
public class UserInfo {
	@Schema(description = "The user's name", required = true, examples = {"Madoka", "Homura"})
	private String name;

	@Schema(description = "The user's age", required = true, examples = {"114", "514"})
	private int age;

	@Schema(description = "The user's fooBar", required = true)
	private FooBar fooBar;

	public UserInfo(String name, int age, FooBar fooBar) {
		this.name = name;
		this.age = age;
		this.fooBar = fooBar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public FooBar getFooBar() {
		return fooBar;
	}

	public void setFooBar(FooBar fooBar) {
		this.fooBar = fooBar;
	}
}
