package test.org.springdoc.api.app181;

public class AbstractParameterObject<T> {

	int primitiveBaseField;

	T genericField;

	public int getPrimitiveBaseField() {
		return primitiveBaseField;
	}

	public void setPrimitiveBaseField(int primitiveBaseField) {
		this.primitiveBaseField = primitiveBaseField;
	}

	public T getGenericField() {
		return genericField;
	}

	public void setGenericField(T genericField) {
		this.genericField = genericField;
	}
}
