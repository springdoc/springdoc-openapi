package test.org.springdoc.api.app25.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	private CoatType coat;

	@Builder
	public Cat(String name, Owner owner, CoatType coat) {
		super(name, owner);
		this.coat = coat;
	}

	public static enum CoatType {
		TABBY, TOROISE, COLORPOINT, BICOLOR, TRICOLOR, SOLID
	}

}
