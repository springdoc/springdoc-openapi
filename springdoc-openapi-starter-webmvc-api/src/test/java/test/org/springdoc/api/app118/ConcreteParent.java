package test.org.springdoc.api.app118;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ChildOfConcrete1.class),
		@Type(ChildOfConcrete2.class)
})
public class ConcreteParent {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class ChildOfConcrete1 extends ConcreteParent {
	private String concreteChild1Param;

	public String getConcreteChild1Param() {
		return concreteChild1Param;
	}

	public void setConcreteChild1Param(String concreteChild1Param) {
		this.concreteChild1Param = concreteChild1Param;
	}
}

class ChildOfConcrete2 extends ConcreteParent {
	private String concreteChild2Param;

	public String getConcreteChild2Param() {
		return concreteChild2Param;
	}

	public void setConcreteChild2Param(String concreteChild2Param) {
		this.concreteChild2Param = concreteChild2Param;
	}
}
