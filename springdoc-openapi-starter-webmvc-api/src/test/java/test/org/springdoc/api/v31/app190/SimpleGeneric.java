package test.org.springdoc.api.v31.app190;

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
