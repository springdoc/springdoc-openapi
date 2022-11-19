package test.org.springdoc.api.app25.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
