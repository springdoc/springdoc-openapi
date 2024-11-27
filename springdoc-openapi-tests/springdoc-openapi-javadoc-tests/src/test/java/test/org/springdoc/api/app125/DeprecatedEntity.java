package test.org.springdoc.api.app125;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Deprecated entity.
 * @author bnasslahsen
 */
class DeprecatedEntity {
	/**
	 * The My non deprecated field.
	 */
	@Schema(deprecated = false)
	private String myNonDeprecatedField;

	/**
	 * The Mydeprecated field.
	 */
	@Schema(deprecated = true)
	private String mydeprecatedField;

	/**
	 * Gets my non deprecated field.
	 *
	 * @return the my non deprecated field
	 */
	public String getMyNonDeprecatedField() {
		return myNonDeprecatedField;
	}

	/**
	 * Sets my non deprecated field.
	 *
	 * @param myNonDeprecatedField the my non deprecated field 
	 * @return the my non deprecated field
	 */
	@Deprecated
	public DeprecatedEntity setMyNonDeprecatedField(String myNonDeprecatedField) {
		this.myNonDeprecatedField = myNonDeprecatedField;
		return this;
	}

	/**
	 * Gets mydeprecated field.
	 *
	 * @return the mydeprecated field
	 */
	public String getMydeprecatedField() {
		return mydeprecatedField;
	}

	/**
	 * Sets mydeprecated field.
	 *
	 * @param mydeprecatedField the mydeprecated field
	 */
	public void setMydeprecatedField(String mydeprecatedField) {
		this.mydeprecatedField = mydeprecatedField;
	}
}