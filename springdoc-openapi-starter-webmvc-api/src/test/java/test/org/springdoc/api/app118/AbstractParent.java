package test.org.springdoc.api.app118;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ChildOfAbstract1.class),
		@Type(ChildOfAbstract2.class)
})
public abstract class AbstractParent {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class ChildOfAbstract1 extends AbstractParent {
	private String abstrachChild1Param;

	public String getAbstrachChild1Param() {
		return abstrachChild1Param;
	}

	public void setAbstrachChild1Param(String abstrachChild1Param) {
		this.abstrachChild1Param = abstrachChild1Param;
	}
}

class ChildOfAbstract2 extends AbstractParent {
	private String abstractChild2Param;

	public String getAbstractChild2Param() {
		return abstractChild2Param;
	}

	public void setAbstractChild2Param(String abstractChild2Param) {
		this.abstractChild2Param = abstractChild2Param;
	}
}
