package test.org.springdoc.api.v31.app193;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a Book.")
public class Book {

	private String title;

	public Book(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
