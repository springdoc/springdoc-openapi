package test.org.springdoc.api.app6.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;


public class Item extends RepresentationModel<Item> {

	private String description;

	public Item(String description) {
		this.description = description;
	}

	public Item(Link initialLink, String description) {
		super(initialLink);
		this.description = description;
	}

	public Item(Iterable<Link> initialLinks, String description) {
		super(initialLinks);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
