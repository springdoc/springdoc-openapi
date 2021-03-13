package test.org.springdoc.api.app153;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(type="string", allowableValues={"finished", "new"})
public enum OrderState {
	FINISHED("finished"),
	NEW("new");

	OrderState(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	private final String value;
}