package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.apache.commons.lang3.ArrayUtils;

public class ServerBuilder {
	/**
	 * Required. A URL to the target host.
	 * This URL supports Server Variables and may be relative, to indicate that the host location is relative to the location where the
	 * OpenAPI definition is being served. Variable substitutions will be made when a variable is named in {brackets}.
	 *
	 **/
	private String url = "";

	/**
	 * An optional string describing the host designated by the URL. CommonMark syntax MAY be used for rich text representation.
	 *
	 **/
	private String description = "";

	/**
	 * An array of variables used for substitution in the server's URL template.
	 *
	 **/
	private ServerVariable[] variables = {};

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};


	private ServerBuilder() {
	}

	public static ServerBuilder builder() {
		return new ServerBuilder();
	}

	public ServerBuilder url(String url) {
		this.url = url;
		return this;
	}

	public ServerBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ServerBuilder variables(ServerVariableBuilder serverVariableBuilder) {
		this.variables = ArrayUtils.add( this.variables, serverVariableBuilder.build());
		return this;
	}

	public ServerBuilder  extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

	public Server build() {
		Server server = new Server() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String url() {
				return url;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public ServerVariable[] variables() {
				return variables;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
		return server;
	}
}
