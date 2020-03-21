/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import java.lang.reflect.Parameter;

import org.springframework.core.MethodParameter;

class ParameterInfo {

	private final MethodParameter methodParameter;

	private String pName;

	private io.swagger.v3.oas.models.parameters.Parameter parameterModel;

	public ParameterInfo(String pName, MethodParameter methodParameter,
			io.swagger.v3.oas.models.parameters.Parameter parameterModel) {
		super();
		this.pName = pName;
		this.methodParameter = methodParameter;
		this.parameterModel = parameterModel;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public MethodParameter getMethodParameter() {
		return methodParameter;
	}

	public Parameter getParameter() {
		return methodParameter.getParameter();
	}

	public io.swagger.v3.oas.models.parameters.Parameter getParameterModel() {
		return parameterModel;
	}

	public void setParameterModel(io.swagger.v3.oas.models.parameters.Parameter parameterModel) {
		this.parameterModel = parameterModel;
	}

}
