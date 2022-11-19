package test.org.springdoc.api.app25.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity

@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "_type")
@JsonSubTypes({
		@Type(value = Dog.class, name = "dog"),
		@Type(value = Cat.class, name = "cat")
})
public class Pet extends BaseEntity {

	@NonNull
	@NotNull
	private String name;

	@NonNull
	@ManyToOne
	private Owner owner;

}
