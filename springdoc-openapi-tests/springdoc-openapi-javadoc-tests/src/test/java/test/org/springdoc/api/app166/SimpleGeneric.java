package test.org.springdoc.api.app166;

class SimpleGeneric<T> {

	/**
	 * Returns name
	 */
	private String name;

	/**
	 * Returns the generic child
	 */
	private T child;

	public T getChild() {
		return child;
	}

	public String getName() {
		return name;
	}
}
