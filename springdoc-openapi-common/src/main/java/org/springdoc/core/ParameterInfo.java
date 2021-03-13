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

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

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
	 * The Required.
	 */
	private boolean required;

	/**
	 * The Default value.
	 */
	private Object defaultValue;

	/**
	 * The Param type.
	 */
	private String paramType;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterInfo.class);


	/**
	 * Instantiates a new Parameter info.
	 *
	 * @param pName the parameter name
	 * @param methodParameter the method parameter
	 * @param parameterBuilder the parameter builder
	 */
	public ParameterInfo(String pName, MethodParameter methodParameter, GenericParameterService parameterBuilder) {
		PropertyResolverUtils propertyResolverUtils = parameterBuilder.getPropertyResolverUtils();
		RequestHeader requestHeader = methodParameter.getParameterAnnotation(RequestHeader.class);
		RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
		PathVariable pathVar = methodParameter.getParameterAnnotation(PathVariable.class);
		CookieValue cookieValue = methodParameter.getParameterAnnotation(CookieValue.class);
		boolean isFile = parameterBuilder.isFile(methodParameter);

		this.methodParameter = methodParameter;
		this.pName = pName;

		if (requestHeader != null)
			calculateParams(requestHeader);
		else if (requestParam != null)
			calculateParams(requestParam, isFile);
		else if (pathVar != null)
			calculateParams(pathVar);
		else if (cookieValue != null)
			calculateParams(cookieValue);

		if (StringUtils.isNotBlank(this.pName))
			this.pName = propertyResolverUtils.resolve(this.pName);
		if (this.defaultValue != null && !ValueConstants.DEFAULT_NONE.equals(this.defaultValue.toString())) {
			this.defaultValue = propertyResolverUtils.resolve(this.defaultValue.toString());
			parameterBuilder.getOptionalWebConversionServiceProvider()
					.ifPresent(conversionService -> {
								try {
									this.defaultValue = conversionService.convert(this.defaultValue, new TypeDescriptor(methodParameter));
								}
								catch (Exception e) {
									LOGGER.warn("Using the following default value : {}, without spring conversionService", this.defaultValue);
								}
							}
					);
		}

		this.required = this.required && !methodParameter.isOptional();
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
	 * Is required boolean.
	 *
	 * @return the boolean
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Gets default value.
	 *
	 * @return the default value
	 */
	public Object getDefaultValue() {
		return defaultValue;
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
	 * Sets required.
	 *
	 * @param required the required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Sets default value.
	 *
	 * @param defaultValue the default value
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Calculate params.
	 *
	 * @param cookieValue the cookie value
	 */
	private void calculateParams(CookieValue cookieValue) {
		if (StringUtils.isNotEmpty(cookieValue.value()))
			this.pName = cookieValue.value();
		this.required = cookieValue.required();
		this.defaultValue = cookieValue.defaultValue();
		this.paramType = ParameterIn.COOKIE.toString();
	}

	/**
	 * Calculate params.
	 *
	 * @param pathVar the path var
	 */
	private void calculateParams(PathVariable pathVar) {
		if (StringUtils.isNotEmpty(pathVar.value()))
			this.pName = pathVar.value();
		this.required = pathVar.required();
		this.paramType = ParameterIn.PATH.toString();
	}

	/**
	 * Calculate params.
	 *
	 * @param requestParam the request param
	 * @param isFile the is file
	 */
	private void calculateParams(RequestParam requestParam, boolean isFile) {
		if (StringUtils.isNotEmpty(requestParam.value()))
			this.pName = requestParam.value();
		this.required = requestParam.required();
		this.defaultValue = requestParam.defaultValue();
		if (!isFile)
			this.paramType = ParameterIn.QUERY.toString();
	}

	/**
	 * Calculate params.
	 *
	 * @param requestHeader the request header
	 */
	private void calculateParams(RequestHeader requestHeader) {
		if (StringUtils.isNotEmpty(requestHeader.value()))
			this.pName = requestHeader.value();
		this.required = requestHeader.required();
		this.defaultValue = requestHeader.defaultValue();
		this.paramType = ParameterIn.HEADER.toString();
	}
}
