package test.org.springdoc.api.v31.app189;

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
