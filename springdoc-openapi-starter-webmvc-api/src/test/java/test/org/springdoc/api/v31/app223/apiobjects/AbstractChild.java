package test.org.springdoc.api.v31.app223.apiobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ChildType1.class),
		@Type(ChildType2.class)
})
public abstract class AbstractChild {
	private int id;

	public AbstractChild(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class ChildType1 extends AbstractChild {
	private String childType1Param;

	public ChildType1(int id, String childType1Param) {
		super(id);
		this.childType1Param = childType1Param;
	}

	public String getChildType1Param() {
		return childType1Param;
	}

	public void setChildType1Param(String childType1Param) {
		this.childType1Param = childType1Param;
	}
}

class ChildType2 extends AbstractChild {
	private String childType2Param;

	public ChildType2(int id, String childType2Param) {
		super(id);
		this.childType2Param = childType2Param;
	}

	public String getChildType2Param() {
		return childType2Param;
	}

	public void setChildType2Param(String childType2Param) {
		this.childType2Param = childType2Param;
	}
}