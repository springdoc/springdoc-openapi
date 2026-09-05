/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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
import java.util.List;
import java.util.regex.Pattern;

import com.scalar.maven.core.ScalarHtmlRenderer;
import com.scalar.maven.core.ScalarProperties;
import com.scalar.maven.core.config.ScalarSource;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_URL;
import static org.springdoc.scalar.ScalarConstants.SCALAR_JS_FILENAME;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Abstract scalar controller.
 *
 * @author  bnasslahsen This is a copy of the class <a href="com.scalar.maven.webjar.ScalarController">ScalarController</a> from the scalar webjar. It has been slightly modified to fit the springdoc-openapi code base.
 */
public abstract class AbstractScalarController {

	/**
	 * The constant SCRIPT_SRC_PATTERN.
	 */
	private static final Pattern SCRIPT_SRC_PATTERN = Pattern.compile("(<script[^>]*\\s+src\\s*=\\s*\")([^\"]*)(\")" );

	/**
	 * The Spring doc config properties.
	 */
	protected final SpringDocConfigProperties springDocConfigProperties;

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
	 * @param springDocConfigProperties the spring doc config properties
	 */
	protected AbstractScalarController(ScalarProperties scalarProperties, SpringDocConfigProperties springDocConfigProperties) {
		this.springDocConfigProperties = springDocConfigProperties;
		this.scalarProperties = scalarProperties;
		this.originalScalarUrl = scalarProperties.getUrl();
	}

	/**
	 * Gets scalar js.
	 *
	 * @return  the scalar js    
	 * @throws IOException the io exception
	 */
	@GetMapping({ DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME, SCALAR_JS_FILENAME })
	@Operation(hidden = true)
	public ResponseEntity<byte[]> getScalarJs() throws IOException {
		byte[] jsContent = ScalarHtmlRenderer.getScalarJsContent();
		return ResponseEntity.ok()
				.contentType(MediaType.valueOf("application/javascript"))
				.body(jsContent);
	}

	/**
	 * Gets docs.
	 *
	 * @param requestUrl the request url  
	 * @param apiDocsPath the api docs path  
	 * @param scalarPath the scalar path  
	 * @return  the docs  
	 * @throws IOException the io exception
	 */
	protected ResponseEntity<String> getDocs(String requestUrl, String apiDocsPath, String scalarPath) throws IOException {
		String html = ScalarHtmlRenderer.render(configureProperties(requestUrl, apiDocsPath));
		String bundleUrl = buildJsBundleUrl(requestUrl, scalarPath);
		html = SCRIPT_SRC_PATTERN.matcher(html).replaceAll("$1" + bundleUrl + "$3");
		return ResponseEntity.ok()
				.contentType(MediaType.TEXT_HTML)
				.body(html);
	}

	/**
	 * Builds the properties used to render the page for one request.
	 * <p>
	 * The URL of the OpenAPI description is derived from the current request, so it is
	 * request state and must not be written back to the {@link ScalarProperties} singleton:
	 * concurrent requests would overwrite each other's URL, and one landing between the two
	 * writes made when groups are configured would render a page whose {@code url} is
	 * {@code null}. The configured properties are therefore copied, and only the copy is
	 * adjusted. {@code BeanUtils} does the copying rather than an explicit list of setters,
	 * so that options added by future scalar versions are carried over automatically.
	 *
	 * @param requestUrl the request url
	 * @param apiDocsPath the api docs path
	 * @return  the scalar properties for this request
	 */
	private ScalarProperties configureProperties(String requestUrl, String apiDocsPath) {
		ScalarProperties properties = new ScalarProperties();
		BeanUtils.copyProperties(scalarProperties, properties);
		String url = buildApiDocsUrl(requestUrl, apiDocsPath);
		List<ScalarSource> scalarSources = springDocConfigProperties.getGroupConfigs().stream()
				.map(groupConfig -> new ScalarSource(url + DEFAULT_PATH_SEPARATOR + groupConfig.getGroup(), groupConfig.getDisplayName(), null, false)).toList();

		if (!CollectionUtils.isEmpty(scalarSources)) {
			properties.setSources(scalarSources);
			properties.setUrl(null);
		}
		else {
			properties.setUrl(url);
		}
		return properties;
	}

	/**
	 * Build js bundle url string.
	 *
	 * @param requestUrl the request url    
	 * @param scalarPath the scalar path    
	 * @return  the string
	 */
	private String buildJsBundleUrl(String requestUrl, String scalarPath) {
		if (SCALAR_DEFAULT_URL.equals(originalScalarUrl) && requestUrl.contains("://")) {
			int firstPathSlash = requestUrl.indexOf('/', requestUrl.indexOf("://") + 3);
			String path = firstPathSlash >= 0 ? requestUrl.substring(firstPathSlash) : "/";
			if (path.endsWith("/"))
				path = path.substring(0, path.length() - 1);
			return path + DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME;
		}
		return scalarPath + DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME;
	}

	/**
	 * Gets api docs url.
	 *
	 * @param requestUrl the request url    
	 * @param apiDocsPath the api docs path    
	 * @return  the api docs url
	 */
	private String buildApiDocsUrl(String requestUrl, String apiDocsPath) {
		String apiDocsUrl = originalScalarUrl;
		if (SCALAR_DEFAULT_URL.equals(originalScalarUrl)) {
			String serverUrl = requestUrl.substring(0, requestUrl.length() - scalarProperties.getPath().length());
			apiDocsUrl = serverUrl + apiDocsPath;
		}
		return apiDocsUrl;
	}
}
