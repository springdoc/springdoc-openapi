package test.org.springdoc.api.app166;

class SimpleOuterClass {

	/**
	 * Returns the name of the outer class
	 */
	private String name;

	/**
	 * Returns the inner class
	 */
	private SimpleInnerClass innerClass;

	public String getName() {
		return name;
	}

	public SimpleInnerClass getInnerClass() {
		return innerClass;
	}
}
