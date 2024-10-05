package test.org.springdoc.api.app155;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Abstract parameter object.
 *
 * @param <T>  the type parameter
 */
class AbstractParameterObject<T extends Enum<T>> {

	/**
	 * The Primitive base field.
	 */
	int primitiveBaseField;

	/**
	 * The Generic field.
	 */
	@Parameter(schema = @Schema(type = "string", allowableValues = { "ONE", "TWO" }))
	T genericField;

	/**
	 * Gets primitive base field.
	 *
	 * @return the primitive base field
	 */
	public int getPrimitiveBaseField() {
		return primitiveBaseField;
	}

	/**
	 * Sets primitive base field.
	 *
	 * @param primitiveBaseField the primitive base field
	 */
	public void setPrimitiveBaseField(int primitiveBaseField) {
		this.primitiveBaseField = primitiveBaseField;
	}

	/**
	 * Gets generic field.
	 *
	 * @return the generic field
	 */
	public T getGenericField() {
		return genericField;
	}

	/**
	 * Sets generic field.
	 *
	 * @param genericField the generic field
	 */
	public void setGenericField(T genericField) {
		this.genericField = genericField;
	}
}
