package test.org.springdoc.api.app156;

import java.util.Set;

/**
 * The type User.
 */
class User {
	/**
	 * The Some text.
	 */
	private String someText;

	/**
	 * The Text set.
	 */
	private Set<String> textSet;

	/**
	 * The Some enums.
	 */
	private Set<SomeEnum> someEnums;

	/**
	 * Gets some text.
	 *
	 * @return the some text
	 */
	public String getSomeText() {
		return someText;
	}

	/**
	 * Sets some text.
	 *
	 * @param someText the some text
	 */
	public void setSomeText(String someText) {
		this.someText = someText;
	}

	/**
	 * Gets text set.
	 *
	 * @return the text set
	 */
	public Set<String> getTextSet() {
		return textSet;
	}

	/**
	 * Sets text set.
	 *
	 * @param textSet the text set
	 */
	public void setTextSet(Set<String> textSet) {
		this.textSet = textSet;
	}

	/**
	 * Gets some enums.
	 *
	 * @return the some enums
	 */
	public Set<SomeEnum> getSomeEnums() {
		return someEnums;
	}

	/**
	 * Sets some enums.
	 *
	 * @param someEnums the some enums
	 */
	public void setSomeEnums(Set<SomeEnum> someEnums) {
		this.someEnums = someEnums;
	}
}
