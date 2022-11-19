package test.org.springdoc.api.app17;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
public @Data
class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "child_property_id")
	private ChildProperty myChildPropertyName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChildProperty getMyChildPropertyName() {
		return myChildPropertyName;
	}

	public void setMyChildPropertyName(ChildProperty myChildPropertyName) {
		this.myChildPropertyName = myChildPropertyName;
	}
}