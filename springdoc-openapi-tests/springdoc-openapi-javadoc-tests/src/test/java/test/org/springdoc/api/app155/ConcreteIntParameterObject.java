package test.org.springdoc.api.app155;

/**
 * The type Concrete int parameter object.
 */
class ConcreteIntParameterObject extends AbstractIntParameterObject<Integer> {

	/**
	 * The Primitive concrete field.
	 */
	int primitiveConcreteField;

	/**
	 * Gets primitive concrete field.
	 *
	 * @return the primitive concrete field
	 */
	public int getPrimitiveConcreteField() {
		return primitiveConcreteField;
	}

	/**
	 * Sets primitive concrete field.
	 *
	 * @param primitiveConcreteField the primitive concrete field
	 */
	public void setPrimitiveConcreteField(int primitiveConcreteField) {
		this.primitiveConcreteField = primitiveConcreteField;
	}
}
