package test.org.springdoc.api.app23;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) private Long id;

	@ManyToMany
	@Size(min = 1)
	@ArraySchema(
			schema = @Schema(
					implementation = String.class,
					accessMode = AccessMode.WRITE_ONLY
			))
	private Set<Clinic> clinics;
}