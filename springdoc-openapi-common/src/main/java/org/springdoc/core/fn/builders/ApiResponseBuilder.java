/*
 *
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Api response builder.
 */
public class ApiResponseBuilder {

	/**
	 * A short description of the response.
	 *
	 */
	private String description = "";

	/**
	 * The HTTP response code, or 'default', for the supplied response. May only have 1 default entry.
	 *
	 */
	private String responseCode = "default";

	/**
	 * An array of response headers. Allows additional information to be included with response.
	 *
	 */
	private Header[] headers = {};

	/**
	 * An array of operation links that can be followed from the response.
	 *
	 */
	private Link[] links = {};

	/**
	 * An array containing descriptions of potential response payloads, for different media types.
	 *
	 */
	private Content[] content = {};

	/**
	 * The list of optional extensions
	 *
	 */
	private Extension[] extensions = {};

	/**
	 * A reference to a response defined in components responses.
	 *
	 * @since swagger -core 2.0.3
	 */
	private String ref = "";


	/**
	 * Instantiates a new Api response builder.
	 */
	private ApiResponseBuilder() {
	}

	/**
	 * Builder api response builder.
	 *
	 * @return the api response builder
	 */
	public static ApiResponseBuilder builder() {
		return new ApiResponseBuilder();
	}

	/**
	 * Description api response builder.
	 *
	 * @param description the description
	 * @return the api response builder
	 */
	public ApiResponseBuilder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Response code api response builder.
	 *
	 * @param responseCode the response code
	 * @return the api response builder
	 */
	public ApiResponseBuilder responseCode(String responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	/**
	 * Header api response builder.
	 *
	 * @param headers the headers
	 * @return the api response builder
	 */
	public ApiResponseBuilder header(HeaderBuilder headers) {
		this.headers = ArrayUtils.add(this.headers, headers.build());
		return this;
	}

	/**
	 * Link api response builder.
	 *
	 * @param linkBuilder the link builder
	 * @return the api response builder
	 */
	public ApiResponseBuilder link(LinkBuilder linkBuilder) {
		this.links = ArrayUtils.add(this.links, linkBuilder.build());
		return this;
	}

	/**
	 * Content api response builder.
	 *
	 * @param contentBuilder the content builder
	 * @return the api response builder
	 */
	public ApiResponseBuilder content(ContentBuilder contentBuilder) {
		this.content = ArrayUtils.add(this.content, contentBuilder.build());
		return this;
	}

	/**
	 * Implementation api response builder.
	 *
	 * @param clazz the clazz
	 * @return the api response builder
	 */
	public ApiResponseBuilder implementation(Class clazz) {
		this.content = ArrayUtils.add(this.content, ContentBuilder.builder().schema(SchemaBuilder.builder().implementation(clazz)).build());
		return this;
	}

	/**
	 * Implementation array api response builder.
	 *
	 * @param clazz the clazz
	 * @return the api response builder
	 */
	public ApiResponseBuilder implementationArray(Class clazz) {
		this.content = ArrayUtils.add(this.content, ContentBuilder.builder().array(ArraySchemaBuilder.builder().schema(SchemaBuilder.builder().implementation(clazz))).build());
		return this;
	}

	/**
	 * Extension api response builder.
	 *
	 * @param extensionBuilder the extension builder
	 * @return the api response builder
	 */
	public ApiResponseBuilder extension(ExtensionBuilder extensionBuilder) {
		this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
		return this;
	}

	/**
	 * Ref api response builder.
	 *
	 * @param ref the ref
	 * @return the api response builder
	 */
	public ApiResponseBuilder ref(String ref) {
		this.ref = ref;
		return this;
	}

	/**
	 * Build api response.
	 *
	 * @return the api response
	 */
	public ApiResponse build() {
		return new ApiResponse() {
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
	}
}
