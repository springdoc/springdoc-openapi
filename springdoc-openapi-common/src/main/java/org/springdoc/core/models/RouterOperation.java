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

package org.springdoc.core.models;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.bind.annotation.RequestMethod;


public class RouterOperation {

	private String path;

	private RequestMethod[] method;

	private String[] consumes;

	private String[] produces;

	private String[] headers;

	private Class<?> beanClass;

	private String beanMethod;

	private Class<?>[] parameterTypes;

	private Operation operation;

	public RouterOperation(org.springdoc.core.annotations.RouterOperation routerOperationAnnotation) {
		this.path = routerOperationAnnotation.path();
		this.method = routerOperationAnnotation.method();
		this.consumes = routerOperationAnnotation.consumes();
		this.produces = routerOperationAnnotation.produces();
		this.beanClass = routerOperationAnnotation.beanClass();
		this.beanMethod = routerOperationAnnotation.beanMethod();
		this.parameterTypes = routerOperationAnnotation.parameterTypes();
		this.operation = routerOperationAnnotation.operation();
		this.headers = routerOperationAnnotation.headers();
	}

	public RouterOperation(org.springdoc.core.annotations.RouterOperation routerOperationAnnotation, RouterFunctionData routerFunctionData) {
		this.path = StringUtils.isBlank(routerOperationAnnotation.path()) ? routerFunctionData.getPath() : routerOperationAnnotation.path();
		this.method = ArrayUtils.isEmpty(routerOperationAnnotation.method()) ? routerFunctionData.getMethods() : routerOperationAnnotation.method();
		this.consumes = ArrayUtils.isEmpty(routerOperationAnnotation.consumes()) ? routerFunctionData.getConsumes() : routerOperationAnnotation.consumes();
		this.produces = routerOperationAnnotation.produces();
		this.beanClass = routerOperationAnnotation.beanClass();
		this.beanMethod = routerOperationAnnotation.beanMethod();
		this.parameterTypes = routerOperationAnnotation.parameterTypes();
		this.operation = routerOperationAnnotation.operation();
		this.headers = ArrayUtils.isEmpty(routerOperationAnnotation.headers()) ? routerFunctionData.getHeaders() : routerOperationAnnotation.headers();

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public RequestMethod[] getMethod() {
		return method;
	}

	public void setMethod(RequestMethod[] method) {
		this.method = method;
	}

	public String[] getConsumes() {
		return consumes;
	}

	public void setConsumes(String[] consumes) {
		this.consumes = consumes;
	}

	public String[] getProduces() {
		return produces;
	}

	public void setProduces(String[] produces) {
		this.produces = produces;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanMethod() {
		return beanMethod;
	}

	public void setBeanMethod(String beanMethod) {
		this.beanMethod = beanMethod;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
}
