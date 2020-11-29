/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.ui;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import nonapi.io.github.classgraph.utils.FileUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webflux.core.SpringDocWebFluxConfiguration;
import org.springdoc.webflux.ui.SwaggerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;


@WebFluxTest
@ContextConfiguration(classes = { SpringDocConfiguration.class, SpringDocConfigProperties.class, SpringDocWebFluxConfiguration.class, SwaggerUiConfigProperties.class, SwaggerConfig.class, SwaggerUiOAuthProperties.class })
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

	@Autowired
	protected WebTestClient webTestClient;

	protected String getContent(String fileName) {
		try {
			Path path = Paths.get(FileUtils.class.getClassLoader().getResource(fileName).toURI());
			byte[] fileBytes = Files.readAllBytes(path);
			return new String(fileBytes, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to read file: " + fileName, e);
		}
	}

}
