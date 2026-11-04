/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v30.app242;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RootModel {

	private Integer rootProperty;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonUnwrapped
	private UnwrappedModelOne unwrappedModelOne;

	private UnwrappedModelTwo unwrappedModelTwo;

	public Integer getRootProperty() {
		return rootProperty;
	}

	public void setRootProperty(Integer rootProperty) {
		this.rootProperty = rootProperty;
	}

	public UnwrappedModelOne getUnwrappedModelOne() {
		return unwrappedModelOne;
	}

	public void setUnwrappedModelOne(UnwrappedModelOne unwrappedModelOne) {
		this.unwrappedModelOne = unwrappedModelOne;
	}

	public UnwrappedModelTwo getUnwrappedModelTwo() {
		return unwrappedModelTwo;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@JsonUnwrapped
	public void setUnwrappedModelTwo(UnwrappedModelTwo unwrappedModelTwo) {
		this.unwrappedModelTwo = unwrappedModelTwo;
	}
}
