package test.org.springdoc.api.app118;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * The type Abstract parent.
 */
@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ChildOfAbstract1.class),
		@Type(ChildOfAbstract2.class)
})
public abstract class AbstractParent {
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
 * The type Child of abstract 1.
 */
class ChildOfAbstract1 extends AbstractParent {
	/**
	 * The Abstrach child 1 param.
	 */
	private String abstrachChild1Param;

	/**
	 * Gets abstrach child 1 param.
	 *
	 * @return the abstrach child 1 param
	 */
	public String getAbstrachChild1Param() {
		return abstrachChild1Param;
	}

	/**
	 * Sets abstrach child 1 param.
	 *
	 * @param abstrachChild1Param the abstrach child 1 param
	 */
	public void setAbstrachChild1Param(String abstrachChild1Param) {
		this.abstrachChild1Param = abstrachChild1Param;
	}
}

/**
 * The type Child of abstract 2.
 */
class ChildOfAbstract2 extends AbstractParent {
	/**
	 * The Abstract child 2 param.
	 */
	private String abstractChild2Param;

	/**
	 * Gets abstract child 2 param.
	 *
	 * @return the abstract child 2 param
	 */
	public String getAbstractChild2Param() {
		return abstractChild2Param;
	}

	/**
	 * Sets abstract child 2 param.
	 *
	 * @param abstractChild2Param the abstract child 2 param
	 */
	public void setAbstractChild2Param(String abstractChild2Param) {
		this.abstractChild2Param = abstractChild2Param;
	}
}
