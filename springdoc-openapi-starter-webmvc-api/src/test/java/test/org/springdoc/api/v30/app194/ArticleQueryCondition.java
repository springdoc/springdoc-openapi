package test.org.springdoc.api.v30.app194;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class ArticleQueryCondition {
	@Schema(description = "title")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
