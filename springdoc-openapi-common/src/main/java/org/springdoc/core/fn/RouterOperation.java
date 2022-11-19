/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
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
import org.springdoc.core.fn.builders.operation.Builder;

import org.springframework.web.bind.annotation.RequestMethod;

import static org.springdoc.core.Constants.OPERATION_ATTRIBUTE;

/**
 * The type Router operation.
 * @author bnasslahsen
 * @author hyeonisism
 */
public class RouterOperation implements Comparable<RouterOperation> {

	/**
	 * The Path.
	 */
	private String path;

	/**
	 * The Methods.
	 */
	private RequestMethod[] methods;

	/**
	 * The Consumes.
	 */
	private String[] consumes;

	/**
	 * The Produces.
	 */
	private String[] produces;

	/**
	 * The Headers.
	 */
	private String[] headers;

	/**
	 * The Params.
	 */
	private String[] params;

	/**
	 * The Bean class.
	 */
	private Class<?> beanClass;

	/**
	 * The Bean method.
	 */
	private String beanMethod;

	/**
	 * The Parameter types.
	 */
	private Class<?>[] parameterTypes;

	/**
	 * The Query params.
	 */
	private Map<String, String> queryParams;

	/**
	 * The Operation.
	 */
	private Operation operation;

	/**
	 * The Operation model.
	 */
	private io.swagger.v3.oas.models.Operation operationModel;

	/**
	 * Instantiates a new Router operation.
	 */
	public RouterOperation() {
	}

	/**
	 * Instantiates a new Router operation.
	 *
	 * @param routerOperationAnnotation the router operation annotation
	 */
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
		this.params = routerOperationAnnotation.params();
	}

	/**
	 * Instantiates a new Router operation.
	 *
	 * @param routerOperationAnnotation the router operation annotation
	 * @param routerFunctionData the router function data
	 */
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
		this.params = routerOperationAnnotation.params();
		this.queryParams = routerFunctionData.getQueryParams();
	}

	/**
	 * Instantiates a new Router operation.
	 *
	 * @param path the path
	 * @param methods the methods
	 * @param consumes the consumes
	 * @param produces the produces
	 * @param headers the headers
	 */
	public RouterOperation(String path, RequestMethod[] methods, String[] consumes, String[] produces, String[] headers, String[] params) {
		this.path = path;
		this.methods = methods;
		this.consumes = consumes;
		this.produces = produces;
		this.headers = headers;
		this.params = params;
	}

	/**
	 * Instantiates a new Router operation.
	 *
	 * @param routerFunctionData the router function data
	 */
	public RouterOperation(RouterFunctionData routerFunctionData) {
		this.path = routerFunctionData.getPath();
		this.methods = routerFunctionData.getMethods();
		this.consumes = routerFunctionData.getConsumes();
		this.produces = routerFunctionData.getProduces();
		this.headers = routerFunctionData.getHeaders();
		this.params = routerFunctionData.getParams();
		this.queryParams = routerFunctionData.getQueryParams();

		Map<String, Object> attributes = routerFunctionData.getAttributes();
		if (attributes.containsKey(OPERATION_ATTRIBUTE)) {
			Builder routerOperationBuilder = (Builder) attributes.get(OPERATION_ATTRIBUTE);
			RouterOperation routerOperation = routerOperationBuilder.build();
			this.beanClass = routerOperation.getBeanClass();
			this.beanMethod = routerOperation.getBeanMethod();
			this.parameterTypes = routerOperation.getParameterTypes();
			this.operation = routerOperation.getOperation();
		}
	}

	/**
	 * Instantiates a new Router operation.
	 *
	 * @param routerOperation the router operation
	 * @param requestMethod the request method
	 */
	public RouterOperation(org.springdoc.core.annotations.RouterOperation routerOperation, RequestMethod requestMethod) {
		this(routerOperation);
		this.methods = new RequestMethod[] { requestMethod };
	}

	/**
	 * Gets path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets path.
	 *
	 * @param path the path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get methods request method [ ].
	 *
	 * @return the request method [ ]
	 */
	public RequestMethod[] getMethods() {
		return methods;
	}

	/**
	 * Sets methods.
	 *
	 * @param methods the methods
	 */
	public void setMethods(RequestMethod[] methods) {
		this.methods = methods;
	}

	/**
	 * Get consumes string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getConsumes() {
		return consumes;
	}

	/**
	 * Sets consumes.
	 *
	 * @param consumes the consumes
	 */
	public void setConsumes(String[] consumes) {
		this.consumes = consumes;
	}

	/**
	 * Get produces string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getProduces() {
		return produces;
	}

	/**
	 * Sets produces.
	 *
	 * @param produces the produces
	 */
	public void setProduces(String[] produces) {
		this.produces = produces;
	}

	/**
	 * Gets bean class.
	 *
	 * @return the bean class
	 */
	public Class<?> getBeanClass() {
		return beanClass;
	}

	/**
	 * Sets bean class.
	 *
	 * @param beanClass the bean class
	 */
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	/**
	 * Gets bean method.
	 *
	 * @return the bean method
	 */
	public String getBeanMethod() {
		return beanMethod;
	}

	/**
	 * Sets bean method.
	 *
	 * @param beanMethod the bean method
	 */
	public void setBeanMethod(String beanMethod) {
		this.beanMethod = beanMethod;
	}

	/**
	 * Get parameter types class [ ].
	 *
	 * @return the class [ ]
	 */
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	/**
	 * Sets parameter types.
	 *
	 * @param parameterTypes the parameter types
	 */
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	/**
	 * Gets operation.
	 *
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * Sets operation.
	 *
	 * @param operation the operation
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * Get headers string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * Sets headers.
	 *
	 * @param headers the headers
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	/**
	 * Gets query params.
	 *
	 * @return the query params
	 */
	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	/**
	 * Sets query params.
	 *
	 * @param queryParams the query params
	 */
	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * Gets params.
	 *
	 * @return the params
	 */
	public String[] getParams() {
		return this.params;
	}

	/**
	 * Sets params.
	 *
	 * @param params
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

	/**
	 * Gets operation model.
	 *
	 * @return the operation model
	 */
	public io.swagger.v3.oas.models.Operation getOperationModel() {
		return operationModel;
	}

	/**
	 * Sets operation model.
	 *
	 * @param operationModel the operation model
	 */
	public void setOperationModel(io.swagger.v3.oas.models.Operation operationModel) {
		this.operationModel = operationModel;
	}

	@Override
	public int compareTo(RouterOperation routerOperation) {
		int result = path.compareTo(routerOperation.getPath());
		if (result == 0 && !ArrayUtils.isEmpty(methods))
			result = methods[0].compareTo(routerOperation.getMethods()[0]);
		if (result == 0 && operationModel != null && routerOperation.getOperationModel() != null)
			result = operationModel.getOperationId().compareTo(routerOperation.getOperationModel().getOperationId());
		if (result == 0 && operation != null)
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
				Arrays.equals(params, that.params) &&
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
		result = 31 * result + Arrays.hashCode(params);
		result = 31 * result + Arrays.hashCode(consumes);
		result = 31 * result + Arrays.hashCode(produces);
		result = 31 * result + Arrays.hashCode(headers);
		result = 31 * result + Arrays.hashCode(parameterTypes);
		return result;
	}
}
