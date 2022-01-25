package test.org.springdoc.api.app156;

import java.util.Set;

public class User {
    private String someText;
    private Set<String> textSet;
    private Set<SomeEnum> someEnums;

	public String getSomeText() {
		return someText;
	}

	public void setSomeText(String someText) {
		this.someText = someText;
	}

	public Set<String> getTextSet() {
		return textSet;
	}

	public void setTextSet(Set<String> textSet) {
		this.textSet = textSet;
	}

	public Set<SomeEnum> getSomeEnums() {
		return someEnums;
	}

	public void setSomeEnums(Set<SomeEnum> someEnums) {
		this.someEnums = someEnums;
	}
}
