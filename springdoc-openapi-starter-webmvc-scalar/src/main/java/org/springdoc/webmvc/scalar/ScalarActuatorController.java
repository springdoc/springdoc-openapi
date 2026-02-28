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

package org.springdoc.webmvc.scalar;

import java.io.IOException;

import com.scalar.maven.webmvc.SpringBootScalarProperties;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.scalar.AbstractScalarController;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;
import static org.springdoc.scalar.ScalarConstants.DEFAULT_SCALAR_ACTUATOR_PATH;
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
	 * @param scalarProperties the scalar properties 
	 * @param springDocConfigProperties the spring doc config properties 
	 * @param webEndpointProperties the web endpoint properties
	 */
	public ScalarActuatorController(SpringBootScalarProperties scalarProperties, SpringDocConfigProperties springDocConfigProperties, WebEndpointProperties webEndpointProperties) {
		super(scalarProperties, springDocConfigProperties);
		this.webEndpointProperties = webEndpointProperties;
	}

	@Operation(hidden = true)
	@GetMapping(DEFAULT_PATH_SEPARATOR)
	public ResponseEntity<String> getDocs(HttpServletRequest request) throws IOException {
		String apiDocsPath = DEFAULT_PATH_SEPARATOR + DEFAULT_API_DOCS_ACTUATOR_URL;
		String requestUrl = request.getRequestURL().toString();
		String scalarPath = webEndpointProperties.getBasePath() + scalarProperties.getPath();
		return getDocs(requestUrl, apiDocsPath, scalarPath);
	}

}
