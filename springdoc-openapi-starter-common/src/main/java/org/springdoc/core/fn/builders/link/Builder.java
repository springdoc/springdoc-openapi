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

package org.springdoc.core.fn.builders.link;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Link builder.
 *
 * @author bnasslahsen
 */
public class Builder {
	/**
	 * The name of this link.
	 */
	private String name = "";

	/**
	 * A relative or absolute reference to an OAS operation. This field is mutually exclusive of the operationId field, and must point to an Operation Object. Relative operationRef values may be used to locate an existing Operation Object in the OpenAPI definition.  Ignored if the operationId property is specified.
	 */
	private String operationRef = "";

	/**
	 * The name of an existing, resolvable OAS operation, as defined with a unique operationId. This field is mutually exclusive of the operationRef field.
	 */
	private String operationId = "";

	/**
	 * Array of parameters to pass to an operation as specified with operationId or identified via operationRef.
	 */
	private LinkParameter[] parameters = {};

	/**
	 * A description of the link. CommonMark syntax may be used for rich text representation.
	 */
	private String description = "";

	/**
	 * A literal value or {expression} to use as a request body when calling the target operation.
	 */
	private String requestBody = "";

	/**
	 * An alternative server to service this operation.
	 */
	private Server server = org.springdoc.core.fn.builders.server.Builder.serverBuilder().build();

	/**
	 * The list of optional extensions
	 */
	private Extension[] extensions = {};

	/**
	 * A reference to a link defined in components links.
	 *
	 * @since swagger -core  2.0.3
	 */
	private String ref = "";


	/**
	 * Instantiates a new Link builder.
	 */
	private Builder() {
	}

	/**
	 * Builder link builder.
	 *
	 * @return the link builder
	 */
	public static Builder linkBuilder() {
		return new Builder();
	}

	/**
	 * Name link builder.
	 *
	 * @param name the name
	 * @return the link builder
	 */
	public Builder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Operation ref link builder.
	 *
	 * @param operationRef the operation ref
	 * @return the link builder
	 */
	public Builder operationRef(String operationRef) {
		this.operationRef = operationRef;
		return this;
	}

	/**
	 * Operation id link builder.
	 *
	 * @param operationId the operation id
	 * @return the link builder
	 */
	public Builder operationId(String operationId) {
		this.operationId = operationId;
		return this;
	}

	/**
	 * Parameter link builder.
	 *
	 * @param linkParameterBuilder the link parameter builder
	 * @return the link builder
	 */
	public Builder parameter(org.springdoc.core.fn.builders.linkparameter.Builder linkParameterBuilder) {
		this.parameters = ArrayUtils.add(this.parameters, linkParameterBuilder.build());
		return this;
	}

	/**
	 * Description link builder.
	 *
	 * @param description the description
	 * @return the link builder
	 */
	public Builder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Request body link builder.
	 *
	 * @param requestBody the request body
	 * @return the link builder
	 */
	public Builder requestBody(String requestBody) {
		this.requestBody = requestBody;
		return this;
	}

	/**
	 * Server link builder.
	 *
	 * @param serverBuilder the server builder
	 * @return the link builder
	 */
	public Builder server(org.springdoc.core.fn.builders.server.Builder serverBuilder) {
		this.server = serverBuilder.build();
		return this;
	}

	/**
	 * Extension link builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the link builder
	 */
	public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Ref link builder.
	 *
	 * @param ref the ref
	 * @return the link builder
	 */
	public Builder ref(String ref) {
		this.ref = ref;
		return this;
	}

	/**
	 * Build link.
	 *
	 * @return the link
	 */
	public Link build() {
		return new Link() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String operationRef() {
				return operationRef;
			}

			@Override
			public String operationId() {
				return operationId;
			}

			@Override
			public LinkParameter[] parameters() {
				return parameters;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public String requestBody() {
				return requestBody;
			}

			@Override
			public Server server() {
				return server;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}

			@Override
			public String ref() {
				return ref;
			}
		};
	}
}
