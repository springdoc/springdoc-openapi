package test.org.springdoc.api.app155;

public class ConcreteParameterObject extends AbstractParameterObject<ConcreteEnum> {

	int primitiveConcreteField;

	public int getPrimitiveConcreteField() {
		return primitiveConcreteField;
	}

	public void setPrimitiveConcreteField(int primitiveConcreteField) {
		this.primitiveConcreteField = primitiveConcreteField;
	}
}
