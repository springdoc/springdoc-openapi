package test.org.springdoc.api.v30.app190;

public class SimpleGeneric<T> {

	private String name;

	private T child;

	public T getChild() {
		return child;
	}

	public String getName() {
		return name;
	}
}
