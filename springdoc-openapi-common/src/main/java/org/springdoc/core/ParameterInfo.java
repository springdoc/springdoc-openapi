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

import org.apache.commons.lang3.StringUtils;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

class ParameterInfo {

	private final MethodParameter methodParameter;

	private String pName;

	private io.swagger.v3.oas.models.parameters.Parameter parameterModel;

	private RequestHeader requestHeader;

	private RequestParam requestParam;

	private PathVariable pathVar;

	private CookieValue cookieValue;

	public ParameterInfo(String pName, MethodParameter methodParameter) {
		this.methodParameter = methodParameter;
		this.requestHeader = methodParameter.getParameterAnnotation(RequestHeader.class);
		this.requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
		this.pathVar = methodParameter.getParameterAnnotation(PathVariable.class);
		this.cookieValue = methodParameter.getParameterAnnotation(CookieValue.class);
		this.pName = calculateName(pName, requestHeader, requestParam, pathVar, cookieValue);
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

	public RequestHeader getRequestHeader() {
		return requestHeader;
	}

	public RequestParam getRequestParam() {
		return requestParam;
	}

	public PathVariable getPathVar() {
		return pathVar;
	}

	public CookieValue getCookieValue() {
		return cookieValue;
	}

	private String calculateName(String pName, RequestHeader requestHeader, RequestParam requestParam, PathVariable pathVar, CookieValue cookieValue) {
		String name = pName;
		if (requestHeader != null && StringUtils.isNotEmpty(requestHeader.value()))
			name = requestHeader.value();
		else if (requestParam != null && StringUtils.isNotEmpty(requestParam.value()))
			name = requestParam.value();
		else if (pathVar != null && StringUtils.isNotEmpty(pathVar.value()))
			name = pathVar.value();
		else if (cookieValue != null && StringUtils.isNotEmpty(cookieValue.value()))
			name = cookieValue.value();
		return name;
	}
}
