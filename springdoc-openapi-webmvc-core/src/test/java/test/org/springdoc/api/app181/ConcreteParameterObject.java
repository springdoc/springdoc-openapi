package test.org.springdoc.api.app181;

public class ConcreteParameterObject extends AbstractParameterObject<String> {

	int primitiveConcreteField;

	public int getPrimitiveConcreteField() {
		return primitiveConcreteField;
	}

	public void setPrimitiveConcreteField(int primitiveConcreteField) {
		this.primitiveConcreteField = primitiveConcreteField;
	}
}
