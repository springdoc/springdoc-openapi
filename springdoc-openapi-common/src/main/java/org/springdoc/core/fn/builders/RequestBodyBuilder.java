package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.lang3.ArrayUtils;

public class RequestBodyBuilder {

	/**
	 * A brief description of the request body.
	 *
	 **/
	private String description = "";

	/**
	 * The content of the request body.
	 *
	 **/
	private Content[] content = {};

	/**
	 * Determines if the request body is required in the request. Defaults to false.
	 *
	 **/
	private boolean required;

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * A reference to a RequestBody defined in components RequestBodies.
	 *
	 * @since swagger-core 2.0.3
	 **/
	private String ref = "";


	private RequestBodyBuilder() {
	}

	public static RequestBodyBuilder builder() {
		return new RequestBodyBuilder();
	}

	public RequestBodyBuilder description(String description) {
		this.description = description;
		return this;
	}

	public RequestBodyBuilder content(ContentBuilder contentBuilder) {
		this.content = ArrayUtils.add( this.content, contentBuilder.build());
		return this;
	}

	public RequestBodyBuilder implementation(Class clazz) {
		this.content = ArrayUtils.add(this.content, ContentBuilder.builder().schema(SchemaBuilder.builder().implementation(clazz)).build());
		return this;
	}

	public RequestBodyBuilder required(boolean required) {
		this.required = required;
		return this;
	}

	public RequestBodyBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add( this.extensions, extensionBuilder.build());
		return this;
	}

	public RequestBodyBuilder ref(String ref) {
		this.ref = ref;
		return this;
	}

	public RequestBody build() {
		RequestBody requestBody = new RequestBody(){

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public Content[] content() {
				return content;
			}

			@Override
			public boolean required() {
				return required;
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
		return requestBody;
	}
}
