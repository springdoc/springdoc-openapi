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

package test.org.springdoc.api.v30.app9.component.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "components")
@Schema(description = "A demo component to illustrate Springdoc Issue #401")
public final class DemoComponentDto {

	@Schema(description = "Some ID", example = "1")
	private String id;

	@Schema(description = "Some dummy payload", example = "Hello World")
	private String payload;

	public DemoComponentDto(String id, String payload) {
		this.id = id;
		this.payload = payload;
	}

	private DemoComponentDto(Builder builder) {
		setId(builder.id);
		setPayload(builder.payload);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public static final class Builder {
		private String id;

		private String payload;

		private Builder() {
		}

		public Builder id(String val) {
			id = val;
			return this;
		}

		public Builder payload(String val) {
			payload = val;
			return this;
		}

		public DemoComponentDto build() {
			return new DemoComponentDto(this);
		}
	}
}
