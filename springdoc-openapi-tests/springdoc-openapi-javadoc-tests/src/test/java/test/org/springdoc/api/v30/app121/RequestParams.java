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

package test.org.springdoc.api.v30.app121;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;


/**
 * The type Request params.
 */
class RequestParams {

	/**
	 * The String param.
	 */
	@Parameter(description = "string parameter")
	private String stringParam;

	/**
	 * The String param 1.
	 */
	@Deprecated
	private String stringParam1;

	/**
	 * The String param 2.
	 */
	@Parameter(description = "string parameter2", required = true)
	private String stringParam2;

	/**
	 * The Int param.
	 */
	@Parameter(description = "int parameter")
	private int intParam;

	/**
	 * The Int param 2.
	 */
	private Optional<String> intParam2;

	/**
	 * The Int param 3.
	 */
	private String intParam3;

	/**
	 * The Nested.
	 */
	private Nested nested;

	/**
	 * The Nested list.
	 */
	private List<Nested> nestedList;

	/**
	 * Gets string param.
	 *
	 * @return the string param
	 */
	public String getStringParam() {
		return stringParam;
	}

	/**
	 * Sets string param.
	 *
	 * @param stringParam the string param
	 */
	public void setStringParam(String stringParam) {
		this.stringParam = stringParam;
	}

	/**
	 * Gets int param.
	 *
	 * @return the int param
	 */
	public int getIntParam() {
		return intParam;
	}

	/**
	 * Sets int param.
	 *
	 * @param intParam the int param
	 */
	public void setIntParam(int intParam) {
		this.intParam = intParam;
	}

	/**
	 * Gets int param 2.
	 *
	 * @return the int param 2
	 */
	public Optional<String> getIntParam2() {
		return intParam2;
	}

	/**
	 * Sets int param 2.
	 *
	 * @param intParam2 the int param 2
	 */
	public void setIntParam2(Optional<String> intParam2) {
		this.intParam2 = intParam2;
	}

	/**
	 * Gets int param 3.
	 *
	 * @return the int param 3
	 */
	public String getIntParam3() {
		return intParam3;
	}

	/**
	 * Sets int param 3.
	 *
	 * @param intParam3 the int param 3
	 */
	public void setIntParam3(String intParam3) {
		this.intParam3 = intParam3;
	}

	/**
	 * Gets string param 1.
	 *
	 * @return the string param 1
	 */
	public String getStringParam1() {
		return stringParam1;
	}

	/**
	 * Sets string param 1.
	 *
	 * @param stringParam1 the string param 1
	 */
	public void setStringParam1(String stringParam1) {
		this.stringParam1 = stringParam1;
	}

	/**
	 * Gets string param 2.
	 *
	 * @return the string param 2
	 */
	public String getStringParam2() {
		return stringParam2;
	}

	/**
	 * Sets string param 2.
	 *
	 * @param stringParam2 the string param 2
	 */
	public void setStringParam2(String stringParam2) {
		this.stringParam2 = stringParam2;
	}

	/**
	 * Gets nested.
	 *
	 * @return the nested
	 */
	public Nested getNested() {
		return nested;
	}

	/**
	 * Sets nested.
	 *
	 * @param nested the nested
	 */
	public void setNested(Nested nested) {
		this.nested = nested;
	}

	/**
	 * Gets nested list.
	 *
	 * @return the nested list
	 */
	public List<Nested> getNestedList() {
		return nestedList;
	}

	/**
	 * Sets nested list.
	 *
	 * @param nestedList the nested list
	 */
	public void setNestedList(List<Nested> nestedList) {
		this.nestedList = nestedList;
	}

	/**
	 * The type Nested.
	 */
	public static class Nested {
		/**
		 * The Param 1.
		 */
		private String param1;

		/**
		 * The Param 2.
		 */
		private BigInteger param2;

		/**
		 * Gets param 1.
		 *
		 * @return the param 1
		 */
		@Parameter(description = "nested string parameter")
		public String getParam1() {
			return param1;
		}

		/**
		 * Sets param 1.
		 *
		 * @param param1 the param 1
		 */
		public void setParam1(String param1) {
			this.param1 = param1;
		}

		/**
		 * Gets param 2.
		 *
		 * @return the param 2
		 */
		@Parameter(description = "nested BigInteger parameter")
		public BigInteger getParam2() {
			return param2;
		}

		/**
		 * Sets param 2.
		 *
		 * @param param2 the param 2
		 */
		public void setParam2(BigInteger param2) {
			this.param2 = param2;
		}
	}
}
