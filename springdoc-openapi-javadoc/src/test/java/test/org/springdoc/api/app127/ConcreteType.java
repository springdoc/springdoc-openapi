package test.org.springdoc.api.app127;

/**
 * The enum Concrete type.
 */
public enum ConcreteType {
	/**
	 * The Type a.
	 */
	TYPE_A("Type A"),
	/**
	 * The Type b.
	 */
	TYPE_B("Type B");

	/**
	 * The Name.
	 */
	private final String name;

	/**
	 * Instantiates a new Concrete type.
	 *
	 * @param name the name
	 */
	ConcreteType(String name) {
    this.name = name;
  }

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
    return name;
  }
}
