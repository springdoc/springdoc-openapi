package test.org.springdoc.api.app25.model;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
public class Clinic extends BaseEntity {

	@NotNull
	private String name;

	@Size(min = 1)
	@ElementCollection
	private Set<Address> addresses;

}
