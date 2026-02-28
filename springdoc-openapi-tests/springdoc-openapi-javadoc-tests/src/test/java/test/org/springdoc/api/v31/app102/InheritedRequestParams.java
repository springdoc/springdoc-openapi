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

package test.org.springdoc.api.v31.app102;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;

/**
 * The type Inherited request params.
 */
class InheritedRequestParams extends RequestParams {
	/**
	 * The Child param.
	 */
	@Parameter(description = "parameter from child of RequestParams")
	@NotBlank
	private String childParam;

	/**
	 * Gets child param.
	 *
	 * @return the child param
	 */
	public String getChildParam() {
		return childParam;
	}

	/**
	 * Sets child param.
	 *
	 * @param childParam the child param
	 */
	public void setChildParam(String childParam) {
		this.childParam = childParam;
	}
}
