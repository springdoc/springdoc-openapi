package test.org.springdoc.api.app9.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import test.org.springdoc.api.app9.application.FooController;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Accessors(fluent = true)
@Builder(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
public class FeedResponse extends RepresentationModel<FeedResponse> {
	@NotNull
	@JsonProperty("data")
	private final List<ResponseData> data;

	public static FeedResponse createForResponseData(@NotNull List<ResponseData> responseData, UUID uuid) {
		var feedResponse = new FeedResponse(responseData);
		feedResponse.add(linkTo(methodOn(FooController.class).getFoo(uuid)).withSelfRel());


		feedResponse.add(linkTo(methodOn(FooController.class).getFoo(uuid)).withRel(IanaLinkRelations.NEXT));

		return feedResponse;
	}
}
