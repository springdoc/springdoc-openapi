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

package org.springdoc.webmvc.scalar;

import java.io.IOException;


import tools.jackson.databind.ObjectMapper;
import com.scalar.maven.webjar.ScalarProperties;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.scalar.AbstractScalarController;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;
import static org.springdoc.scalar.ScalarConstants.DEFAULT_SCALAR_ACTUATOR_PATH;
import static org.springdoc.scalar.ScalarConstants.SCALAR_JS_FILENAME;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger actuator welcome.
 */
@ControllerEndpoint(id = DEFAULT_SCALAR_ACTUATOR_PATH)
public class ScalarActuatorController extends AbstractScalarController {

	/**
	 * The Web endpoint properties.
	 */
	private final WebEndpointProperties webEndpointProperties;

	/**
	 * Instantiates a new Scalar actuator controller.
	 *
	 * @param scalarProperties      the scalar properties
	 * @param webEndpointProperties the web endpoint properties
	 * @param objectMapper          the object mapper
	 */
	protected ScalarActuatorController(ScalarProperties scalarProperties, WebEndpointProperties webEndpointProperties, ObjectMapper objectMapper) {
		super(scalarProperties, objectMapper);
		this.webEndpointProperties = webEndpointProperties;
	}

	/**
	 * Gets docs.
	 *
	 * @param request the request
	 * @return the docs
	 * @throws IOException the io exception
	 */
	@Operation(hidden = true)
	@GetMapping(DEFAULT_PATH_SEPARATOR)
	public ResponseEntity<String> getDocs(HttpServletRequest request) throws IOException {
		return super.getDocs(request.getRequestURL().toString());
	}

	@Operation(hidden = true)
	@GetMapping(DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME)
	public ResponseEntity<byte[]> getScalarJs() throws IOException {
		return super.getScalarJs();
	}

	public String buildApiDocsUrl(String requestUrl) {
		return buildApiDocsUrl(requestUrl, DEFAULT_PATH_SEPARATOR + DEFAULT_API_DOCS_ACTUATOR_URL);
	}

	public String buildJsBundleUrl(String requestUrl) {
		String scalarPath = webEndpointProperties.getBasePath() + scalarProperties.getPath();
		return buildJsBundleUrl(requestUrl, scalarPath);
	}
}
