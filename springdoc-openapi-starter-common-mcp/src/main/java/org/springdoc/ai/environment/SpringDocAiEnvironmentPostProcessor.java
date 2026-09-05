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

package org.springdoc.ai.environment;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import static org.springdoc.ai.properties.SpringDocAiProperties.SPRINGDOC_MCP_ENABLED;

/**
 * An {@link EnvironmentPostProcessor} that forces
 * {@code springdoc.pre-loading-enabled=true} when the AI MCP integration is explicitly
 * enabled. This ensures the OpenAPI specification is generated at startup, making it
 * available for MCP tool registration.
 * <p>
 * The MCP integration is opt-in, so pre-loading is only forced when
 * {@code springdoc.ai.mcp.enabled} is explicitly set to a truthy value. Merely having an
 * MCP starter on the classpath must not change the host application's behaviour.
 *
 * @author bnasslahsen
 */
public class SpringDocAiEnvironmentPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (Boolean.parseBoolean(environment.getProperty(SPRINGDOC_MCP_ENABLED))) {
			environment.getPropertySources()
				.addLast(new MapPropertySource("springdoc-ai-defaults",
						Map.of("springdoc.pre-loading-enabled", "true")));
		}
	}

}
