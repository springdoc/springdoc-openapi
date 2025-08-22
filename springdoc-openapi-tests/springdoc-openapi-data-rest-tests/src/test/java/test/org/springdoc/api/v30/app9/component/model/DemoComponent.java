/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v30.app9.component.model;


import org.springframework.data.annotation.Id;

public class DemoComponent {

	@Id
	private String id;

	private String payload;

	public DemoComponent(String id, String payload) {
		this.id = id;
		this.payload = payload;
	}

	private DemoComponent(Builder builder) {
		setId(builder.id);
		setPayload(builder.payload);
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

		public Builder() {
		}

		public Builder id(String val) {
			id = val;
			return this;
		}

		public Builder payload(String val) {
			payload = val;
			return this;
		}

		public DemoComponent build() {
			return new DemoComponent(this);
		}
	}
}
