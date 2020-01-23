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

class ParameterInfo {

	private final java.lang.reflect.Parameter parameter;

	private final int index;

	private String pName;

	private io.swagger.v3.oas.models.parameters.Parameter parameterModel;

	public ParameterInfo(String pName, Parameter parameter,
			io.swagger.v3.oas.models.parameters.Parameter parameterModel, int index) {
		super();
		this.pName = pName;
		this.parameter = parameter;
		this.parameterModel = parameterModel;
		this.index = index;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public java.lang.reflect.Parameter getParameter() {
		return parameter;
	}

	public io.swagger.v3.oas.models.parameters.Parameter getParameterModel() {
		return parameterModel;
	}

	public void setParameterModel(io.swagger.v3.oas.models.parameters.Parameter parameterModel) {
		this.parameterModel = parameterModel;
	}

	public int getIndex() {
		return index;
	}
}
