package test.org.springdoc.api.v30.app223.apiobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ParentType1.class),
		@Type(ParentType2.class)
})
public abstract class AbstractParent {
	private int id;

	public AbstractParent(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class ParentType1 extends AbstractParent {
	private String parentType1Param;
	private AbstractChild abstractChild;

	public ParentType1(int id, String parentType1Param, AbstractChild abstractChild) {
		super(id);
		this.parentType1Param = parentType1Param;
		this.abstractChild = abstractChild;
	}

	public String getParentType1Param() {
		return parentType1Param;
	}

	public void setParentType1Param(String parentType1Param) {
		this.parentType1Param = parentType1Param;
	}

	public AbstractChild getAbstractChild() {
		return abstractChild;
	}

	public void setAbstractChild(AbstractChild abstractChild) {
		this.abstractChild = abstractChild;
	}
}

class ParentType2 extends AbstractParent {
	private String parentType2Param;

	public ParentType2(int id, String parentType2Param) {
		super(id);
		this.parentType2Param = parentType2Param;
	}

	public String getParentType2Param() {
		return parentType2Param;
	}

	public void setParentType2Param(String parentType2Param) {
		this.parentType2Param = parentType2Param;
	}
}
