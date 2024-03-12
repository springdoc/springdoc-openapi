package test.org.springdoc.api.v31.app8;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "the foo bar", deprecated = true)
public class FooBar {
	private int foo;

	public FooBar(int foo) {
		this.foo = foo;
	}

	@Schema(description = "foo", required = true, examples = {"1", "2"})
	public int getFoo() {
		return foo;
	}

	public void setFoo(int foo) {
		this.foo = foo;
	}

	public static void main(String[] args) {
		FooBar fooBar = new FooBar(1);
		System.out.println("Foo: " + fooBar.getFoo());
	}
}






