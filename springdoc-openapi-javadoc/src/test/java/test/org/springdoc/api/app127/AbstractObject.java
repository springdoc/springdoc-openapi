package test.org.springdoc.api.app127;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The type Abstract object.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConcreteObjectA.class, name = "Type A")
})
public abstract class AbstractObject {

	/**
	 * The Type.
	 */
	private final ConcreteType type;

	/**
	 * The Name.
	 */
	private final String name;

	/**
	 * Instantiates a new Abstract object.
	 *
	 * @param type the type 
	 * @param name the name
	 */
	protected AbstractObject(ConcreteType type, String name) {
    this.type = type;
    this.name = name;
  }

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	public ConcreteType getType() {
    return type;
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
