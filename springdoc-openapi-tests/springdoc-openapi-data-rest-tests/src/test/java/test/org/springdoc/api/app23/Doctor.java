package test.org.springdoc.api.app23;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Size;
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