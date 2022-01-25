package test.org.springdoc.api.app4;

public class Album {

	private String title;

	private String description;

	private String releaseDate;

	public Album(String title, String description, String releaseDate) {
		this.title = title;
		this.description = description;
		this.releaseDate = releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

}