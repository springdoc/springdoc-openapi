package test.org.springdoc.api.app155;

public class ConcreteIntParameterObject extends AbstractIntParameterObject<Integer>{

	int primitiveConcreteField;

	public int getPrimitiveConcreteField() {
		return primitiveConcreteField;
	}

	public void setPrimitiveConcreteField(int primitiveConcreteField) {
		this.primitiveConcreteField = primitiveConcreteField;
	}
}
