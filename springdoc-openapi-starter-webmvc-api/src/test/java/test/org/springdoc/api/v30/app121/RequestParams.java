/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app121;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;


public class RequestParams {

	@Parameter(description = "string parameter")
	private String stringParam;

	@Deprecated
	private String stringParam1;

	@Parameter(description = "string parameter2", required = true)
	private String stringParam2;

	@Parameter(description = "int parameter")
	private int intParam;

	private Optional<String> intParam2;

	private String intParam3;

	private Nested nested;

	private List<Nested> nestedList;

	public String getStringParam() {
		return stringParam;
	}

	public void setStringParam(String stringParam) {
		this.stringParam = stringParam;
	}

	public int getIntParam() {
		return intParam;
	}

	public void setIntParam(int intParam) {
		this.intParam = intParam;
	}

	public Optional<String> getIntParam2() {
		return intParam2;
	}

	public void setIntParam2(Optional<String> intParam2) {
		this.intParam2 = intParam2;
	}

	public String getIntParam3() {
		return intParam3;
	}

	public void setIntParam3(String intParam3) {
		this.intParam3 = intParam3;
	}

	public String getStringParam1() {
		return stringParam1;
	}

	public void setStringParam1(String stringParam1) {
		this.stringParam1 = stringParam1;
	}

	public String getStringParam2() {
		return stringParam2;
	}

	public void setStringParam2(String stringParam2) {
		this.stringParam2 = stringParam2;
	}

	public Nested getNested() {
		return nested;
	}

	public void setNested(Nested nested) {
		this.nested = nested;
	}

	public List<Nested> getNestedList() {
		return nestedList;
	}

	public void setNestedList(List<Nested> nestedList) {
		this.nestedList = nestedList;
	}

	public static class Nested {
		private String param1;

		private BigInteger param2;

		@Parameter(description = "nested string parameter")
		public String getParam1() {
			return param1;
		}

		public void setParam1(String param1) {
			this.param1 = param1;
		}

		@Parameter(description = "nested BigInteger parameter")
		public BigInteger getParam2() {
			return param2;
		}

		public void setParam2(BigInteger param2) {
			this.param2 = param2;
		}
	}
}
