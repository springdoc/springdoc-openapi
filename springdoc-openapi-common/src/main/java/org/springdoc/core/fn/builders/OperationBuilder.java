package org.springdoc.core.fn.builders;

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

import org.springframework.util.Assert;

public class OperationBuilder {
	/**
	 * The HTTP method for this operation.
	 *
	 **/
	private String method = "";

	/**
	 * Tags can be used for logical grouping of operations by resources or any other qualifier.
	 *
	 **/
	private String[] tags = {};

	/**
	 * Provides a brief description of this operation. Should be 120 characters or less for proper visibility in Swagger-UI.
	 *
	 **/
	private String summary = "";

	/**
	 * A verbose description of the operation.
	 *
	 **/
	private String description = "";

	/**
	 * Request body associated to the operation.
	 *
	 */
	private RequestBody requestBody = RequestBodyBuilder.builder().build();

	/**
	 * Additional external documentation for this operation.
	 *
	 **/
	private ExternalDocumentation externalDocs = ExternalDocumentationBuilder.builder().build();

	/**
	 * The operationId is used by third-party tools to uniquely identify this operation.
	 *
	 **/
	private String operationId = "";

	/**
	 * An optional array of parameters which will be added to any automatically detected parameters in the method itself.
	 *
	 **/
	private Parameter[] parameters = {};

	/**
	 * The list of possible responses as they are returned from executing this operation.
	 *
	 **/
	private ApiResponse[] responses = {};

	/**
	 * Allows an operation to be marked as deprecated.  Alternatively use the @Deprecated annotation
	 *
	 **/
	private boolean deprecated;

	/**
	 * A declaration of which security mechanisms can be used for this operation.
	 *
	 */
	private SecurityRequirement[] security = {};

	/**
	 * An alternative server array to service this operation.
	 *
	 **/
	private Server[] servers = {};

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * Allows this operation to be marked as hidden
	 *
	 */
	private boolean hidden;

	/**
	 * Ignores JsonView annotations while resolving operations and types.
	 *
	 */
	private boolean ignoreJsonView;

	private OperationBuilder() {
	}

	public static OperationBuilder builder() {
		return new OperationBuilder();
	}

	public OperationBuilder method(String method) {
		this.method = method;
		return this;
	}

	public OperationBuilder tags(String[] tags) {
		this.tags = tags;
		return this;
	}

	public OperationBuilder summary(String summary) {
		this.summary = summary;
		return this;
	}

	public OperationBuilder description(String description) {
		this.description = description;
		return this;
	}

	public OperationBuilder requestBody(RequestBodyBuilder requestBodyBuilder) {
		this.requestBody = requestBodyBuilder.build();
		return this;
	}

	public OperationBuilder externalDocs(ExternalDocumentationBuilder externalDocumentationBuilder) {
		this.externalDocs = externalDocumentationBuilder.build();
		return this;
	}

	public OperationBuilder operationId(String operationId) {
		this.operationId = operationId;
		return this;
	}

	public OperationBuilder parameter(ParameterBuilder parameterBuilder) {
		this.parameters = ArrayUtils.add( this.parameters, parameterBuilder.build() );
		return this;
	}

	public OperationBuilder response(ApiResponseBuilder apiResponseBuilder) {
		this.responses =  ArrayUtils.add( this.responses, apiResponseBuilder.build() );
		return this;
	}

	public OperationBuilder deprecated(boolean deprecated) {
		this.deprecated = deprecated;
		return this;
	}

	public OperationBuilder security(SecurityRequirementBuilder securityRequirementBuilder) {
		this.security = ArrayUtils.add( this.security, securityRequirementBuilder.build() );
		return this;
	}

	public OperationBuilder servers(ServerBuilder serverBuilder) {
		this.servers = ArrayUtils.add( this.servers, serverBuilder.build() );
		return this;
	}

	public OperationBuilder extensions(ExtensionBuilder extensionBuilder) {
		this.extensions =  ArrayUtils.add( this.extensions, extensionBuilder.build() );
		return this;
	}

	public OperationBuilder hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public OperationBuilder ignoreJsonView(boolean ignoreJsonView) {
		this.ignoreJsonView = ignoreJsonView;
		return this;
	}

	public Operation build() {
		Assert.hasLength(operationId, "operationId can not be empty");

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
		return operation;
	}
}
