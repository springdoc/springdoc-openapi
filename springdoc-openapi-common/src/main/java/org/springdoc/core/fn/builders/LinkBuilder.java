package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.commons.lang3.ArrayUtils;

public class LinkBuilder {
	/**
	 * The name of this link.
	 *
	 **/
	private String name = "";

	/**
	 * A relative or absolute reference to an OAS operation. This field is mutually exclusive of the operationId field, and must point to an Operation Object. Relative operationRef values may be used to locate an existing Operation Object in the OpenAPI definition.  Ignored if the operationId property is specified.
	 *
	 **/
	private String operationRef = "";

	/**
	 * The name of an existing, resolvable OAS operation, as defined with a unique operationId. This field is mutually exclusive of the operationRef field.
	 *
	 **/
	private String operationId = "";

	/**
	 * Array of parameters to pass to an operation as specified with operationId or identified via operationRef.
	 *
	 **/
	private LinkParameter[] parameters = {};

	/**
	 * A description of the link. CommonMark syntax may be used for rich text representation.
	 *
	 **/
	private String description = "";

	/**
	 * A literal value or {expression} to use as a request body when calling the target operation.
	 *
	 **/
	private String requestBody = "";

	/**
	 * An alternative server to service this operation.
	 *
	 **/
	private Server server = ServerBuilder.builder().build();

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * A reference to a link defined in components links.
	 *
	 * @since swagger-core  2.0.3
	 **/
	private String ref = "";


	private LinkBuilder() {
	}

	public static LinkBuilder builder() {
		return new LinkBuilder();
	}

	public LinkBuilder name(String name) {
		this.name = name;
		return this;
	}

	public LinkBuilder operationRef(String operationRef) {
		this.operationRef = operationRef;
		return this;
	}

	public LinkBuilder operationId(String operationId) {
		this.operationId = operationId;
		return this;
	}

	public LinkBuilder parameter(LinkParameterBuilder linkParameterBuilder) {
		this.parameters = ArrayUtils.add( this.parameters, linkParameterBuilder.build());
		return this;
	}

	public LinkBuilder description(String description) {
		this.description = description;
		return this;
	}

	public LinkBuilder requestBody(String requestBody) {
		this.requestBody = requestBody;
		return this;
	}

	public LinkBuilder server(ServerBuilder serverBuilder) {
		this.server = serverBuilder.build();
		return this;
	}

	public LinkBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

	public LinkBuilder ref(String ref) {
		this.ref = ref;
		return this;
	}

	public Link build() {
		Link link = new Link() {

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

		return link;
	}
}
