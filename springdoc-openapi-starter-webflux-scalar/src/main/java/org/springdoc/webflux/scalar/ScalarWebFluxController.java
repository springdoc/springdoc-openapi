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

package org.springdoc.webflux.scalar;

import java.io.IOException;

import com.scalar.maven.webflux.SpringBootScalarProperties;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.scalar.AbstractScalarController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;

/**
 * The type Scalar web mvc controller.
 *
 * @author bnasslahsen
 */
@Controller
@RequestMapping("${scalar.path:" + SCALAR_DEFAULT_PATH + "}")
public class ScalarWebFluxController extends AbstractScalarController {

	/**
	 * Instantiates a new Scalar web mvc controller.
	 *
	 * @param scalarProperties          the scalar properties
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public ScalarWebFluxController(SpringBootScalarProperties scalarProperties, SpringDocConfigProperties springDocConfigProperties) {
		super(scalarProperties, springDocConfigProperties);
	}

	/**
	 * Gets docs.
	 *
	 * @param serverHttpRequest the server http request
	 * @return the docs
	 * @throws IOException the io exception
	 */
	@GetMapping
	public ResponseEntity<String> getDocs(ServerHttpRequest serverHttpRequest) throws IOException {
		String apiDocsPath = springDocConfigProperties.getApiDocs().getPath();
		String requestUrl = serverHttpRequest.getURI().toString();
		String scalarPath = scalarProperties.getPath();
		return getDocs(requestUrl, apiDocsPath, scalarPath);
	}

}
