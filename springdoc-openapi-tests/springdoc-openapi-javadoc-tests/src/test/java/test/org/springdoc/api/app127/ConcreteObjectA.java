package test.org.springdoc.api.app127;

/**
 * The type Concrete object a.
 */
class ConcreteObjectA extends AbstractObject {

	/**
	 * The Description.
	 */
	private final String description;

	/**
	 * Instantiates a new Concrete object a.
	 *
	 * @param name the name 
	 * @param description the description
	 */
	public ConcreteObjectA(String name, String description) {
		super(ConcreteType.TYPE_A, name);
		this.description = description;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
