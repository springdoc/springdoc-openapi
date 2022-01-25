package test.org.springdoc.api.app155;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

public class AbstractParameterObject<T extends Enum<T>> {

	int primitiveBaseField;

	@Parameter(schema=@Schema(type = "string", allowableValues = {"ONE", "TWO"}) )
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
