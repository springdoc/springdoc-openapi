package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type External documentation builder.
 */
public class ExternalDocumentationBuilder {

	/**
	 * A short description of the target documentation.
	 *
	 */
	private String description="";

	/**
	 * The URL for the target documentation. Value must be in the format of a URL.
	 *
	 */
	private String url="";

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};


	/**
	 * Instantiates a new External documentation builder.
	 */
	private ExternalDocumentationBuilder() {
	}

	/**
	 * An external documentation external documentation builder.
	 *
	 * @return the external documentation builder
	 */
	public static ExternalDocumentationBuilder builder() {
		return new ExternalDocumentationBuilder();
	}

	/**
	 * Description external documentation builder.
	 *
	 * @param description the description
	 * @return the external documentation builder
	 */
	public ExternalDocumentationBuilder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Url external documentation builder.
	 *
	 * @param url the url
	 * @return the external documentation builder
	 */
	public ExternalDocumentationBuilder url(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Extensions external documentation builder.
	 *
	 * @param extensionBuilder the extensions
	 * @return the external documentation builder
	 */
	public ExternalDocumentationBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Build external documentation.
	 *
	 * @return the external documentation
	 */
	public ExternalDocumentation build() {
		ExternalDocumentation externalDocumentation = new ExternalDocumentation() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}
			@Override
			public String description() {
				return description;
			}

			@Override
			public String url() {
				return url;
			}

			@Override
			public Extension[] extensions() {
				return extensions;
			}
		};
		return externalDocumentation;
	}
}
