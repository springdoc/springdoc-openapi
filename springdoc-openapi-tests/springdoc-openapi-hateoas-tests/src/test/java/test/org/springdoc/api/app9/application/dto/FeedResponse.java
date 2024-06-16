package test.org.springdoc.api.app9.application.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import test.org.springdoc.api.app9.application.FooController;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;

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
