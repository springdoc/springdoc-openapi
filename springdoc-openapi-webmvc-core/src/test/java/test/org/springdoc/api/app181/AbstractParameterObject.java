package test.org.springdoc.api.app181;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

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
