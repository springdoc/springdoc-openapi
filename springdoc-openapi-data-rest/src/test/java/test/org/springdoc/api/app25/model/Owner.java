package test.org.springdoc.api.app25.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

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
