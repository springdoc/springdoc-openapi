package test.org.springdoc.api.app5.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * @author bnasslahsen
 */
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "companies", itemRelation = "company")
public class CompanyDto extends RepresentationModel<CompanyDto> {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UUID id;
	@NotNull
	private String name;
}