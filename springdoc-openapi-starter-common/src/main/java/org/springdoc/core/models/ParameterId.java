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

package org.springdoc.core.models;

import java.util.Objects;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Parameter Id.
 *
 * @author bnasslahsen
 */
public class ParameterId {

	/**
	 * The P name.
	 */
	private String pName;

	/**
	 * The Param type.
	 */
	private String paramType;

	/**
	 * The Ref.
	 */
	private String $ref;

	/**
	 * Instantiates a new Parameter id.
	 *
	 * @param parameter the parameter
	 */
	public ParameterId(Parameter parameter) {
		this.pName = parameter.getName();
		this.paramType = parameter.getIn();
		this.$ref = parameter.get$ref();
	}

	/**
	 * Instantiates a new Parameter id.
	 *
	 * @param parameter the parameter
	 */
	public ParameterId(io.swagger.v3.oas.annotations.Parameter parameter) {
		this.pName = parameter.name();
		this.paramType = StringUtils.isNotBlank(parameter.in().toString()) ? parameter.in().toString() : null;
		this.$ref = StringUtils.isNotBlank(parameter.ref()) ? parameter.ref() : null;
	}

	/**
	 * Instantiates a new Parameter id.
	 *
	 * @param pName     the p name
	 * @param paramType the param type
	 */
	public ParameterId(String pName, String paramType) {
		this.pName = pName;
		this.paramType = paramType;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getpName() {
		return pName;
	}

	/**
	 * Sets name.
	 *
	 * @param pName the p name
	 */
	public void setpName(String pName) {
		this.pName = pName;
	}

	/**
	 * Gets param type.
	 *
	 * @return the param type
	 */
	public String getParamType() {
		return paramType;
	}

	/**
	 * Sets param type.
	 *
	 * @param paramType the param type
	 */
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	/**
	 * Get ref string.
	 *
	 * @return the string
	 */
	public String get$ref() {
		return $ref;
	}

	/**
	 * Set ref.
	 *
	 * @param $ref the ref
	 */
	public void set$ref(String $ref) {
		this.$ref = $ref;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ParameterId that = (ParameterId) o;
		if (this.pName == null && StringUtils.isBlank(this.paramType))
			return Objects.equals($ref, that.$ref);
		if (this.pName != null && StringUtils.isBlank(this.paramType))
			return Objects.equals(pName, that.pName);

		return Objects.equals(pName, that.pName) && Objects.equals(paramType, that.paramType);

	}

	@Override
	public int hashCode() {
		return Objects.hash(pName, paramType, $ref);
	}
}
