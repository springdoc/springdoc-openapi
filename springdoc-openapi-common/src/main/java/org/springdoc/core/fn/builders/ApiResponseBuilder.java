package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.ArrayUtils;

public class ApiResponseBuilder {

	/**
	 * A short description of the response.
	 *
	 **/
	private String description = "";

	/**
	 * The HTTP response code, or 'default', for the supplied response. May only have 1 default entry.
	 *
	 **/
	private String responseCode = "default";

	/**
	 * An array of response headers. Allows additional information to be included with response.
	 *
	 **/
	private Header[] headers = {};

	/**
	 * An array of operation links that can be followed from the response.
	 *
	 **/
	private Link[] links = {};

	/**
	 * An array containing descriptions of potential response payloads, for different media types.
	 *
	 **/
	private Content[] content = {};

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * A reference to a response defined in components responses.
	 *
	 * @since swagger-core 2.0.3
	 **/
	private String ref = "";


	private ApiResponseBuilder() {
	}

	public static ApiResponseBuilder builder() {
		return new ApiResponseBuilder();
	}

	public ApiResponseBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ApiResponseBuilder responseCode(String responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	public ApiResponseBuilder header(HeaderBuilder headers) {
		this.headers = ArrayUtils.add(this.headers, headers.build());
		return this;
	}

	public ApiResponseBuilder link(LinkBuilder linkBuilder) {
		this.links = ArrayUtils.add(this.links, linkBuilder.build());
		return this;
	}

	public ApiResponseBuilder content(ContentBuilder contentBuilder) {
		this.content = ArrayUtils.add(this.content, contentBuilder.build());
		return this;
	}

	public ApiResponseBuilder implementation(Class clazz) {
		this.content = ArrayUtils.add(this.content, ContentBuilder.builder().schema(SchemaBuilder.builder().implementation(clazz)).build());
		return this;
	}

	public ApiResponseBuilder implementationArray(Class clazz) {
		this.content = ArrayUtils.add(this.content, ContentBuilder.builder().array(ArraySchemaBuilder.builder().schema(SchemaBuilder.builder().implementation(clazz))).build());
		return this;
	}

	public ApiResponseBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	public ApiResponseBuilder ref(String ref) {
		this.ref = ref;
		return this;
	}

	public ApiResponse build() {
		ApiResponse apiResponse = new ApiResponse() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public String responseCode() {
				return responseCode;
			}

			@Override
			public Header[] headers() {
				return headers;
			}

			@Override
			public Link[] links() {
				return links;
			}

			@Override
			public Content[] content() {
				return content;
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
		return apiResponse;
	}
}
