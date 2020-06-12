/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
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

/**
 * The type Parameter info.
 * @author bnasslahsen
 */
public class ParameterInfo {

	/**
	 * The Method parameter.
	 */
	private final MethodParameter methodParameter;

	/**
	 * The P name.
	 */
	private String pName;

	/**
	 * The Parameter model.
	 */
	private io.swagger.v3.oas.models.parameters.Parameter parameterModel;

	/**
	 * The Request header.
	 */
	private RequestHeader requestHeader;

	/**
	 * The Request param.
	 */
	private RequestParam requestParam;

	/**
	 * The Path var.
	 */
	private PathVariable pathVar;

	/**
	 * The Cookie value.
	 */
	private CookieValue cookieValue;

	/**
	 * Instantiates a new Parameter info.
	 *
	 * @param pName the p name
	 * @param methodParameter the method parameter
	 */
	public ParameterInfo(String pName, MethodParameter methodParameter) {
		this.methodParameter = methodParameter;
		this.requestHeader = methodParameter.getParameterAnnotation(RequestHeader.class);
		this.requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
		this.pathVar = methodParameter.getParameterAnnotation(PathVariable.class);
		this.cookieValue = methodParameter.getParameterAnnotation(CookieValue.class);
		this.pName = calculateName(pName, requestHeader, requestParam, pathVar, cookieValue);
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
	 * Gets method parameter.
	 *
	 * @return the method parameter
	 */
	public MethodParameter getMethodParameter() {
		return methodParameter;
	}

	/**
	 * Gets parameter.
	 *
	 * @return the parameter
	 */
	public Parameter getParameter() {
		return methodParameter.getParameter();
	}

	/**
	 * Gets parameter model.
	 *
	 * @return the parameter model
	 */
	public io.swagger.v3.oas.models.parameters.Parameter getParameterModel() {
		return parameterModel;
	}

	/**
	 * Sets parameter model.
	 *
	 * @param parameterModel the parameter model
	 */
	public void setParameterModel(io.swagger.v3.oas.models.parameters.Parameter parameterModel) {
		this.parameterModel = parameterModel;
	}

	/**
	 * Gets request header.
	 *
	 * @return the request header
	 */
	public RequestHeader getRequestHeader() {
		return requestHeader;
	}

	/**
	 * Gets request param.
	 *
	 * @return the request param
	 */
	public RequestParam getRequestParam() {
		return requestParam;
	}

	/**
	 * Gets path var.
	 *
	 * @return the path var
	 */
	public PathVariable getPathVar() {
		return pathVar;
	}

	/**
	 * Gets cookie value.
	 *
	 * @return the cookie value
	 */
	public CookieValue getCookieValue() {
		return cookieValue;
	}

	/**
	 * Calculate name string.
	 *
	 * @param pName the p name
	 * @param requestHeader the request header
	 * @param requestParam the request param
	 * @param pathVar the path var
	 * @param cookieValue the cookie value
	 * @return the string
	 */
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
