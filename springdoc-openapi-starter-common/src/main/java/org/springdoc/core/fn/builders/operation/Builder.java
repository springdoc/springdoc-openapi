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

package org.springdoc.core.fn.builders.operation;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.fn.RouterOperation;

/**
 * The type Operation builder.
 *
 * @author bnasslahsen
 */
public class Builder {

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
	 * The HTTP method for this operation.
	 */
	private String method = "";

	/**
	 * Tags can be used for logical grouping of operations by resources or any other qualifier.
	 */
	private String[] tags = {};

	/**
	 * Provides a brief description of this operation. Should be 120 characters or less for proper visibility in Swagger-UI.
	 */
	private String summary = "";

	/**
	 * A verbose description of the operation.
	 */
	private String description = "";

	/**
	 * Request body associated to the operation.
	 */
	private RequestBody requestBody = org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder().build();

	/**
	 * Additional external documentation for this operation.
	 */
	private ExternalDocumentation externalDocs = org.springdoc.core.fn.builders.externaldocumentation.Builder.externalDocumentationBuilder().build();

	/**
	 * The operationId is used by third-party tools to uniquely identify this operation.
	 */
	private String operationId = "";

	/**
	 * An optional array of parameters which will be added to any automatically detected parameters in the method itself.
	 */
	private Parameter[] parameters = {};

	/**
	 * The list of possible responses as they are returned from executing this operation.
	 */
	private ApiResponse[] responses = {};

	/**
	 * Allows an operation to be marked as deprecated.  Alternatively use the @Deprecated annotation
	 */
	private boolean deprecated;

	/**
	 * A declaration of which security mechanisms can be used for this operation.
	 */
	private SecurityRequirement[] security = {};

	/**
	 * An alternative server array to service this operation.
	 */
	private Server[] servers = {};

	/**
	 * The list of optional extensions
	 */
	private Extension[] extensions = {};

	/**
	 * Allows this operation to be marked as hidden
	 */
	private boolean hidden;

	/**
	 * Ignores JsonView annotations while resolving operations and types.
	 */
	private boolean ignoreJsonView;

	/**
	 * Instantiates a new Operation builder.
	 */
	private Builder() {
	}

	/**
	 * Builder operation builder.
	 *
	 * @return the operation builder
	 */
	public static Builder operationBuilder() {
		return new Builder();
	}

	/**
	 * Method operation builder.
	 *
	 * @param method the method
	 * @return the operation builder
	 */
	public Builder method(String method) {
		this.method = method;
		return this;
	}

	/**
	 * Tags operation builder.
	 *
	 * @param tags the tags
	 * @return the operation builder
	 */
	public Builder tags(String[] tags) {
		this.tags = tags;
		return this;
	}

	/**
	 * Tag operation builder.
	 *
	 * @param tag the tag
	 * @return the operation builder
	 */
	public Builder tag(String tag) {
		this.tags = ArrayUtils.add(tags, tag);
		return this;
	}

	/**
	 * Summary operation builder.
	 *
	 * @param summary the summary
	 * @return the operation builder
	 */
	public Builder summary(String summary) {
		this.summary = summary;
		return this;
	}

	/**
	 * Description operation builder.
	 *
	 * @param description the description
	 * @return the operation builder
	 */
	public Builder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Request body operation builder.
	 *
	 * @param requestBodyBuilder the request body builder
	 * @return the operation builder
	 */
	public Builder requestBody(org.springdoc.core.fn.builders.requestbody.Builder requestBodyBuilder) {
		this.requestBody = requestBodyBuilder.build();
		return this;
	}

	/**
	 * External docs operation builder.
	 *
	 * @param externalDocumentationBuilder the external documentation builder
	 * @return the operation builder
	 */
	public Builder externalDocs(org.springdoc.core.fn.builders.externaldocumentation.Builder externalDocumentationBuilder) {
		this.externalDocs = externalDocumentationBuilder.build();
		return this;
	}

	/**
	 * Operation id operation builder.
	 *
	 * @param operationId the operation id
	 * @return the operation builder
	 */
	public Builder operationId(String operationId) {
		this.operationId = operationId;
		return this;
	}

	/**
	 * Parameter operation builder.
	 *
	 * @param parameterBuilder the parameter builder
	 * @return the operation builder
	 */
	public Builder parameter(org.springdoc.core.fn.builders.parameter.Builder parameterBuilder) {
		this.parameters = ArrayUtils.add(this.parameters, parameterBuilder.build());
		return this;
	}

	/**
	 * Response operation builder.
	 *
	 * @param apiResponseBuilder the api response builder
	 * @return the operation builder
	 */
	public Builder response(org.springdoc.core.fn.builders.apiresponse.Builder apiResponseBuilder) {
		this.responses = ArrayUtils.add(this.responses, apiResponseBuilder.build());
		return this;
	}

	/**
	 * Deprecated operation builder.
	 *
	 * @param deprecated the deprecated
	 * @return the operation builder
	 */
	public Builder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	/**
	 * Security operation builder.
	 *
	 * @param securityRequirementBuilder the security requirement builder
	 * @return the operation builder
	 */
	public Builder security(org.springdoc.core.fn.builders.securityrequirement.Builder securityRequirementBuilder) {
		this.security = ArrayUtils.add(this.security, securityRequirementBuilder.build());
		return this;
	}

	/**
	 * Servers operation builder.
	 *
	 * @param serverBuilder the server builder
	 * @return the operation builder
	 */
	public Builder servers(org.springdoc.core.fn.builders.server.Builder serverBuilder) {
		this.servers = ArrayUtils.add(this.servers, serverBuilder.build());
		return this;
	}

	/**
	 * Extensions operation builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the operation builder
	 */
	public Builder extensions(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Hidden operation builder.
	 *
	 * @param hidden the hidden
	 * @return the operation builder
	 */
	public Builder hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	/**
	 * Ignore json view operation builder.
	 *
	 * @param ignoreJsonView the ignore json view
	 * @return the operation builder
	 */
	public Builder ignoreJsonView(boolean ignoreJsonView) {
		this.ignoreJsonView = ignoreJsonView;
		return this;
	}

	/**
	 * Bean class operation builder.
	 *
	 * @param beanClass the bean class
	 * @return the operation builder
	 */
	public Builder beanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
		return this;
	}

	/**
	 * Bean method operation builder.
	 *
	 * @param beanMethod the bean method
	 * @return the operation builder
	 */
	public Builder beanMethod(String beanMethod) {
		this.beanMethod = beanMethod;
		return this;
	}

	/**
	 * Parameter types operation builder.
	 *
	 * @param parameterTypes the parameter types
	 * @return the operation builder
	 */
	public Builder parameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
		return this;
	}

	/**
	 * Build operation.
	 *
	 * @return the operation
	 */
	public RouterOperation build() {

		Operation operation = new Operation() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String method() {
				return method;
			}

			@Override
			public String[] tags() {
				return tags;
			}

			@Override
			public String summary() {
				return summary;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public RequestBody requestBody() {
				return requestBody;
			}

			@Override
			public ExternalDocumentation externalDocs() {
				return externalDocs;
			}

			@Override
			public String operationId() {
				return operationId;
			}

			@Override
			public Parameter[] parameters() {
				return parameters;
			}

			@Override
			public ApiResponse[] responses() {
				return responses;
			}

			@Override
			public boolean deprecated() {
				return deprecated;
			}

			@Override
			public SecurityRequirement[] security() {
				return security;
			}

			@Override
			public Server[] servers() {
				return servers;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}

			@Override
			public boolean hidden() {
				return hidden;
			}

			@Override
			public boolean ignoreJsonView() {
				return ignoreJsonView;
			}
		};

		if (StringUtils.isEmpty(operation.operationId()) && (beanClass == null && beanMethod == null && parameterTypes == null))
			throw new IllegalStateException("You should either fill, the Operation or at least the bean class and the bean method");

		if (beanClass != null && beanMethod == null)
			throw new IllegalStateException("The bean method, should not null");

		if (StringUtils.isEmpty(operation.operationId()) && (beanClass == null && beanMethod == null))
			throw new IllegalStateException("operationId can not be empty");

		RouterOperation routerOperation = new RouterOperation();
		routerOperation.setBeanClass(beanClass);
		routerOperation.setBeanMethod(beanMethod);
		routerOperation.setParameterTypes(parameterTypes);
		routerOperation.setOperation(operation);
		return routerOperation;

	}
}
