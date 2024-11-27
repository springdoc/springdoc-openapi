package test.org.springdoc.api.app155;

/**
 * The type Abstract int parameter object.
 *
 * @param <T>  the type parameter
 */
class AbstractIntParameterObject<T extends Integer> {

	/**
	 * The Primitive base field.
	 */
	int primitiveBaseField;

	/**
	 * The Generic field.
	 */
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
