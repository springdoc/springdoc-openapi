package test.org.springdoc.api.app155;

public class AbstractIntParameterObject<T extends Integer> {

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
