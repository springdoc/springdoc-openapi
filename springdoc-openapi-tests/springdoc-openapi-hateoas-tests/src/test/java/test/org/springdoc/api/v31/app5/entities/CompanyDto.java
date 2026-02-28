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

package test.org.springdoc.api.v31.app5.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotNull;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * @author bnasslahsen
 */
@Relation(collectionRelation = "companies", itemRelation = "company")
public class CompanyDto extends RepresentationModel<CompanyDto> {
	@JsonProperty(access = Access.READ_ONLY)
	private UUID id;

	@NotNull
	private String name;

	public CompanyDto(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	public CompanyDto(Link initialLink, UUID id, String name) {
		super(initialLink);
		this.id = id;
		this.name = name;
	}

	public CompanyDto(Iterable<Link> initialLinks, UUID id, String name) {
		super(initialLinks);
		this.id = id;
		this.name = name;
	}

	private CompanyDto(Builder builder) {
		setId(builder.id);
		setName(builder.name);
	}

	public static Builder builder() {
		return new Builder();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static final class Builder {
		private UUID id;

		private @NotNull String name;

		private Builder() {
		}

		public Builder id(UUID val) {
			id = val;
			return this;
		}

		public Builder name(@NotNull String val) {
			name = val;
			return this;
		}

		public CompanyDto build() {
			return new CompanyDto(this);
		}
	}
}