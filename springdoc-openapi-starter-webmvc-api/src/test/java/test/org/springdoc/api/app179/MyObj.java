package test.org.springdoc.api.app179;

public class MyObj {
	private final String id;
	private final String content;

	public MyObj(String id, String content) {
		this.id = id;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}