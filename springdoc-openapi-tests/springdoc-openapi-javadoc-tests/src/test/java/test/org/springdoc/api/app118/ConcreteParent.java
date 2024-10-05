package test.org.springdoc.api.app118;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * The type Concrete parent.
 */
@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ChildOfConcrete1.class),
		@Type(ChildOfConcrete2.class)
})
class ConcreteParent {
	/**
	 * The Id.
	 */
	private int id;

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(int id) {
		this.id = id;
	}
}

/**
 * The type Child of concrete 1.
 */
class ChildOfConcrete1 extends ConcreteParent {
	/**
	 * The Concrete child 1 param.
	 */
	private String concreteChild1Param;

	/**
	 * Gets concrete child 1 param.
	 *
	 * @return the concrete child 1 param
	 */
	public String getConcreteChild1Param() {
		return concreteChild1Param;
	}

	/**
	 * Sets concrete child 1 param.
	 *
	 * @param concreteChild1Param the concrete child 1 param
	 */
	public void setConcreteChild1Param(String concreteChild1Param) {
		this.concreteChild1Param = concreteChild1Param;
	}
}

/**
 * The type Child of concrete 2.
 */
class ChildOfConcrete2 extends ConcreteParent {
	/**
	 * The Concrete child 2 param.
	 */
	private String concreteChild2Param;

	/**
	 * Gets concrete child 2 param.
	 *
	 * @return the concrete child 2 param
	 */
	public String getConcreteChild2Param() {
		return concreteChild2Param;
	}

	/**
	 * Sets concrete child 2 param.
	 *
	 * @param concreteChild2Param the concrete child 2 param
	 */
	public void setConcreteChild2Param(String concreteChild2Param) {
		this.concreteChild2Param = concreteChild2Param;
	}
}
