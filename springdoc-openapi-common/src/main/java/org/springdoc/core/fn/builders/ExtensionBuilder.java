package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Extension builder.
 */
public class ExtensionBuilder {

	/**
	 * An option name for these extensions.
	 *
	 */
	private String name =  "";

	/**
	 * The extension properties.
	 *
	 */
	private ExtensionProperty[] properties;

	/**
	 * Instantiates a new Extension builder.
	 */
	private ExtensionBuilder() {
	}

	/**
	 * Builder extension builder.
	 *
	 * @return the extension builder
	 */
	public static ExtensionBuilder builder() {
		return new ExtensionBuilder();
	}

	/**
	 * Name extension builder.
	 *
	 * @param name the name
	 * @return the extension builder
	 */
	public ExtensionBuilder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Properties extension builder.
	 *
	 * @param extensionPropertyBuilder the properties
	 * @return the extension builder
	 */
	public ExtensionBuilder propertie(ExtensionPropertyBuilder extensionPropertyBuilder) {
		this.properties = ArrayUtils.add( this.properties, extensionPropertyBuilder.build());
		return this;
	}

	/**
	 * Build extension.
	 *
	 * @return the extension
	 */
	public Extension build() {
		Extension extension = new Extension(){
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public ExtensionProperty[] properties() {
				return properties;
			}
		};
		return extension;
	}
}
