package test.org.springdoc.api.app166;

public class LocalErrorResponseDto {

	private String localMessage;

	public LocalErrorResponseDto(String localMessage) {
		this.localMessage = localMessage;
	}

	public String getLocalMessage() {
		return localMessage;
	}

	public void setLocalMessage(String localMessage) {
		this.localMessage = localMessage;
	}
}
