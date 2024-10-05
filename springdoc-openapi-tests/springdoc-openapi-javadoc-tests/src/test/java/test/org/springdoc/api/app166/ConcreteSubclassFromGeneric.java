package test.org.springdoc.api.app166;

class ConcreteSubclassFromGeneric extends SimpleGeneric<MyData> {

	/**
	 * Return the top name
	 */
	private String topName;

	public String getTopName() {
		return topName;
	}
}
