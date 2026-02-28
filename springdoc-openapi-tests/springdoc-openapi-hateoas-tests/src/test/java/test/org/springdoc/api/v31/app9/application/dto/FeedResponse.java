/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app9.application.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import test.org.springdoc.api.v31.app9.application.FooController;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class FeedResponse extends RepresentationModel<FeedResponse> {
	
	@NotNull
	@JsonProperty("data")
	private final List<ResponseData> data;

	public FeedResponse(List<ResponseData> data) {
		this.data = data;
	}

	public static FeedResponse createForResponseData(@NotNull List<ResponseData> responseData, UUID uuid) {
		var feedResponse = new FeedResponse(responseData);
		feedResponse.add(linkTo(methodOn(FooController.class).getFoo(uuid)).withSelfRel());


		feedResponse.add(linkTo(methodOn(FooController.class).getFoo(uuid)).withRel(IanaLinkRelations.NEXT));

		return feedResponse;
	}

	public List<ResponseData> getData() {
		return data;
	}
}
