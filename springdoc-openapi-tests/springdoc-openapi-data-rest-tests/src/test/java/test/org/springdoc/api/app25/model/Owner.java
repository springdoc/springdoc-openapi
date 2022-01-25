package test.org.springdoc.api.app25.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
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
public class Owner extends BaseEntity {

	private String firstname;
	
	@NotNull
	private String lastname;
	
	@NotNull
	@Embedded
	private Address addresses;
	
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "owner", cascade = { CascadeType.ALL })
	private Set<Pet> pets;

}
