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

package org.springdoc.core.fn;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.bind.annotation.RequestMethod;


public class RouterOperation implements Comparable<RouterOperation> {

	private String path;

	private RequestMethod[] methods;

	private String[] consumes;

	private String[] produces;

	private String[] headers;

	private Class<?> beanClass;

	private String beanMethod;

	private Class<?>[] parameterTypes;

	private Map<String, String> queryParams;

	private Operation operation;

	private io.swagger.v3.oas.models.Operation operationModel;

	public RouterOperation(org.springdoc.core.annotations.RouterOperation routerOperationAnnotation) {
		this.path = routerOperationAnnotation.path();
		this.methods = routerOperationAnnotation.method();
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
		this.methods = ArrayUtils.isEmpty(routerOperationAnnotation.method()) ? routerFunctionData.getMethods() : routerOperationAnnotation.method();
		this.consumes = ArrayUtils.isEmpty(routerOperationAnnotation.consumes()) ? routerFunctionData.getConsumes() : routerOperationAnnotation.consumes();
		this.produces = ArrayUtils.isEmpty(routerOperationAnnotation.produces()) ? routerFunctionData.getProduces() : routerOperationAnnotation.produces();
		this.beanClass = routerOperationAnnotation.beanClass();
		this.beanMethod = routerOperationAnnotation.beanMethod();
		this.parameterTypes = routerOperationAnnotation.parameterTypes();
		this.operation = routerOperationAnnotation.operation();
		this.headers = ArrayUtils.isEmpty(routerOperationAnnotation.headers()) ? routerFunctionData.getHeaders() : routerOperationAnnotation.headers();
		this.queryParams = routerFunctionData.getQueryParams();
	}

	public RouterOperation(String path, RequestMethod[] methods) {
		this.path = path;
		this.methods = methods;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public RequestMethod[] getMethods() {
		return methods;
	}

	public void setMethods(RequestMethod[] methods) {
		this.methods = methods;
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

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	public io.swagger.v3.oas.models.Operation getOperationModel() {
		return operationModel;
	}

	public void setOperationModel(io.swagger.v3.oas.models.Operation operationModel) {
		this.operationModel = operationModel;
	}

	@Override
	public int compareTo(RouterOperation routerOperation) {
		int result = path.compareTo(routerOperation.getPath());
		if (result == 0)
			result = methods[0].compareTo(routerOperation.getMethods()[0]);
		if (result == 0 && operationModel != null && routerOperation.getOperationModel() != null)
			result = operationModel.getOperationId().compareTo(routerOperation.getOperationModel().getOperationId());
		if (result == 0 && operation != null && operation.operationId() != null && routerOperation.getOperation().operationId() != null)
			result = operation.operationId().compareTo(routerOperation.getOperation().operationId());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RouterOperation that = (RouterOperation) o;
		return Objects.equals(path, that.path) &&
				Arrays.equals(methods, that.methods) &&
				Arrays.equals(consumes, that.consumes) &&
				Arrays.equals(produces, that.produces) &&
				Arrays.equals(headers, that.headers) &&
				Objects.equals(beanClass, that.beanClass) &&
				Objects.equals(beanMethod, that.beanMethod) &&
				Arrays.equals(parameterTypes, that.parameterTypes) &&
				Objects.equals(queryParams, that.queryParams) &&
				Objects.equals(operation, that.operation) &&
				Objects.equals(operationModel, that.operationModel);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(path, beanClass, beanMethod, queryParams, operation, operationModel);
		result = 31 * result + Arrays.hashCode(methods);
		result = 31 * result + Arrays.hashCode(consumes);
		result = 31 * result + Arrays.hashCode(produces);
		result = 31 * result + Arrays.hashCode(headers);
		result = 31 * result + Arrays.hashCode(parameterTypes);
		return result;
	}
}
