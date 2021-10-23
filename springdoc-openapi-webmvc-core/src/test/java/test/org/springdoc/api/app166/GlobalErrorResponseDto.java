package test.org.springdoc.api.app166;

public class GlobalErrorResponseDto {

	private String globalMessage;

	public GlobalErrorResponseDto(String globalMessage) {
		this.globalMessage = globalMessage;
	}

	public String getGlobalMessage() {
		return globalMessage;
	}

	public void setGlobalMessage(String globalMessage) {
		this.globalMessage = globalMessage;
	}
}
