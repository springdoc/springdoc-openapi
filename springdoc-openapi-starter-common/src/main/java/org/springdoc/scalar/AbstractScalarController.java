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

package org.springdoc.scalar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.scalar.maven.webjar.ScalarProperties;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_URL;
import static org.springdoc.scalar.ScalarConstants.SCALAR_JS_FILENAME;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Abstract scalar controller.
 *
 * @author bnasslahsen This is a copy of the class <a href="com.scalar.maven.webjar.ScalarController">ScalarController</a> from the scalar webjar. It has been slightly modified to fit the springdoc-openapi code base.
 */
public abstract class AbstractScalarController {

	/**
	 * The Scalar properties.
	 */
	protected final ScalarProperties scalarProperties;

	/**
	 * The Original scalar url.
	 */
	protected final String originalScalarUrl;

	/**
	 * Instantiates a new Abstract scalar controller.
	 *
	 * @param scalarProperties the scalar properties
	 */
	protected AbstractScalarController(ScalarProperties scalarProperties) {
		this.scalarProperties = scalarProperties;
		this.originalScalarUrl = scalarProperties.getUrl();
	}

	/**
	 * Serves the main API reference interface.
	 * <p>This endpoint returns an HTML page that displays the Scalar API Reference
	 * interface. The page is configured with the OpenAPI specification URL from
	 * the properties.</p>
	 *
	 * @param requestUrl the request url
	 * @return a ResponseEntity containing the HTML content for the API reference interface
	 * @throws IOException if the HTML template cannot be loaded
	 */
	protected ResponseEntity<String> getDocs(String requestUrl) throws IOException {
		// Load the template HTML
		InputStream inputStream = getClass().getResourceAsStream("/META-INF/resources/webjars/scalar/index.html");
		if (inputStream == null) {
			return ResponseEntity.notFound().build();
		}

		String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		requestUrl = decode(requestUrl);
		// Replace the placeholders with actual values
		String cdnUrl = buildJsBundleUrl(requestUrl);
		String injectedHtml = html
				.replace("__JS_BUNDLE_URL__", cdnUrl)
				.replace("__CONFIGURATION__", """
						    {
						      url: "%s"
						    }
						""".formatted(buildApiDocsUrl(requestUrl)));

		return ResponseEntity.ok()
				.contentType(MediaType.TEXT_HTML)
				.body(injectedHtml);
	}

	/**
	 * Serves the JavaScript bundle for the Scalar API Reference.
	 * <p>This endpoint returns the JavaScript file that powers the Scalar API Reference
	 * interface. The file is served with the appropriate MIME type.</p>
	 *
	 * @return a ResponseEntity containing the JavaScript bundle
	 * @throws IOException if the JavaScript file cannot be loaded
	 */
	protected ResponseEntity<byte[]> getScalarJs() throws IOException {
		// Load the scalar.js file
		InputStream inputStream = getClass().getResourceAsStream("/META-INF/resources/webjars/scalar/" + SCALAR_JS_FILENAME);
		if (inputStream == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] jsContent = inputStream.readAllBytes();

		return ResponseEntity.ok()
				.contentType(MediaType.valueOf("application/javascript"))
				.body(jsContent);
	}

	/**
	 * Decode string.
	 *
	 * @param requestURI the request uri
	 * @return the string
	 */
	protected String decode(String requestURI) {
		return URLDecoder.decode(requestURI, StandardCharsets.UTF_8);
	}

	/**
	 * Gets api docs url.
	 *
	 * @param requestUrl  the request url
	 * @param apiDocsPath the api docs path
	 * @return the api docs url
	 */
	protected String buildApiDocsUrl(String requestUrl, String apiDocsPath) {
		String apiDocsUrl = scalarProperties.getUrl();
		if (SCALAR_DEFAULT_URL.equals(originalScalarUrl)) {
			String serverUrl = requestUrl.substring(0, requestUrl.length() - scalarProperties.getPath().length());
			apiDocsUrl = serverUrl + apiDocsPath;
		}
		return apiDocsUrl;
	}

	/**
	 * Build js bundle url string.
	 *
	 * @param requestUrl the request url
	 * @param scalarPath the scalar path
	 * @return the string
	 */
	protected String buildJsBundleUrl(String requestUrl, String scalarPath) {
		if (SCALAR_DEFAULT_URL.equals(originalScalarUrl)) {
			int firstPathSlash = requestUrl.indexOf('/', requestUrl.indexOf("://") + 3);
			String path = firstPathSlash >= 0 ? requestUrl.substring(firstPathSlash) : "/";
			if( path.endsWith("/"))
				path = path.substring(0, path.length() - 1);
			return path + DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME;
		}
		return scalarPath + DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME;
	}

	/**
	 * Gets api docs url.
	 *
	 * @param requestUrl the request url
	 * @return the api docs url
	 */
	protected abstract String buildApiDocsUrl(String requestUrl);

	/**
	 * Build js bundle url string.
	 *
	 * @param requestUrl the request url
	 * @return the string
	 */
	protected abstract String buildJsBundleUrl(String requestUrl);
}
