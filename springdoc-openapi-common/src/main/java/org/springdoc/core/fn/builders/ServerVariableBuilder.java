package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.apache.commons.lang3.ArrayUtils;

public class ServerVariableBuilder {
	/**
	 * Required.  The name of this variable.
	 *
	 **/
	private String name;

	/**
	 * An array of allowable values for this variable.  This field map to the enum property in the OAS schema.
	 *
	 * @return String array of allowableValues
	 **/
	private String[] allowableValues = {};

	/**
	 * Required.  The default value of this variable.
	 *
	 **/
	private String defaultValue;

	/**
	 * An optional description for the server variable.
	 *
	 **/
	private String description = "";

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	private ServerVariableBuilder() {
	}

	public static ServerVariableBuilder builder() {
		return new ServerVariableBuilder();
	}

	public ServerVariableBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ServerVariableBuilder allowableValues(String[] allowableValues) {
		this.allowableValues = allowableValues;
		return this;
	}

	public ServerVariableBuilder defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public ServerVariableBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ServerVariableBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	public ServerVariable build() {
		ServerVariable serverVariable = new ServerVariable() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String[] allowableValues() {
				return allowableValues;
			}

			@Override
			public String defaultValue() {
				return defaultValue;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
		return serverVariable;
	}
}
