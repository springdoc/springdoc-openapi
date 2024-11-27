package test.org.springdoc.api.app127;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Umbrella.
 */
class Umbrella {

	/**
	 * The Object.
	 */
	@Schema(description = "This reference to abstract class causes weird YAML tag to be added", anyOf = ConcreteObjectA.class)
	private final AbstractObject object;

	/**
	 * Instantiates a new Umbrella.
	 *
	 * @param object the object
	 */
	public Umbrella(AbstractObject object) {
		this.object = object;
	}

	/**
	 * Gets object.
	 *
	 * @return the object
	 */
	public AbstractObject getObject() {
		return object;
	}
}
