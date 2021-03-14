package test.org.springdoc.api.app25.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Doctor extends BaseEntity {

	@NotNull
	private String firstname;
	
	@NotNull
	private String lastname;
	
	@Enumerated(EnumType.STRING)
	private Specialty specialty; 

	@Size(min = 1)
	@ManyToMany
	private Set<Clinic> clinics;

}
