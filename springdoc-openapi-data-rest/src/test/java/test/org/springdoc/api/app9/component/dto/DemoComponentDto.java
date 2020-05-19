package test.org.springdoc.api.app9.component.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "components")
@Schema(description = "A demo component to illustrate Springdoc Issue #401")
public final class DemoComponentDto {

	@Schema(description = "Some ID", example = "1")
	private String id;

	@Schema(description = "Some dummy payload", example = "Hello World")
	private String payload;

}
