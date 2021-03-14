package test.org.springdoc.api.app25.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cat extends Pet {

	public static enum CoatType {
		TABBY, TOROISE, COLORPOINT, BICOLOR, TRICOLOR, SOLID
	}
	
	@Enumerated(EnumType.STRING)
	private CoatType coat;

	@Builder
	public Cat(String name, Owner owner, CoatType coat) {
		super(name, owner);
		this.coat = coat;
	}

}
