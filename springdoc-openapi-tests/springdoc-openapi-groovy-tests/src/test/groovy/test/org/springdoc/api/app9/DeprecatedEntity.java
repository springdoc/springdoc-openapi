package test.org.springdoc.api.app9;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author bnasslahsen
 */
public class DeprecatedEntity
{
	@Schema(deprecated = false)
	private String myNonDeprecatedField;

	@Schema(deprecated = true)
	private String mydeprecatedField;

	public String getMyNonDeprecatedField()
	{
		return myNonDeprecatedField;
	}

	@Deprecated
	public DeprecatedEntity setMyNonDeprecatedField(String myNonDeprecatedField)
	{
		this.myNonDeprecatedField = myNonDeprecatedField;
		return this;
	}

	public String getMydeprecatedField() {
		return mydeprecatedField;
	}

	public void setMydeprecatedField(String mydeprecatedField) {
		this.mydeprecatedField = mydeprecatedField;
	}
}