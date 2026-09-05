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

package test.org.springdoc.ai.mcp.optin;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springdoc.ai.dashboard.McpAuditEventStore;
import org.springdoc.ai.dashboard.McpDashboardController;
import org.springdoc.ai.mcp.OpenApiMcpToolCallbackProvider;
import org.springdoc.ai.properties.SpringDocAiProperties;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the MCP surface is opt-in: having the starter on the classpath registers
 * nothing until {@code springdoc.ai.mcp.enabled=true} is set explicitly, and the
 * dashboard needs its own {@code springdoc.ai.mcp.dashboard-enabled=true}.
 *
 * @author bnasslahsen
 */
class McpOptInTest {

	/**
	 * The test application.
	 */
	@SpringBootApplication
	static class TestApp {

	}

	/**
	 * With no MCP property set, none of the MCP beans exist.
	 *
	 * @author bnasslahsen
	 */
	@Nested
	@SpringBootTest(classes = TestApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	class WhenPropertyUnset {

		/**
		 * The application context.
		 */
		@Autowired
		private ApplicationContext context;

		/**
		 * No MCP bean is registered when the property is absent.
		 */
		@Test
		void registersNoMcpBeans() {
			assertThat(context.getBeanNamesForType(SpringDocAiProperties.class)).isEmpty();
			assertThat(context.getBeanNamesForType(OpenApiMcpToolCallbackProvider.class)).isEmpty();
			assertThat(context.getBeanNamesForType(McpDashboardController.class)).isEmpty();
			assertThat(context.getBeanNamesForType(McpAuditEventStore.class)).isEmpty();
		}

		/**
		 * The environment post-processor does not force pre-loading when MCP is not
		 * explicitly enabled.
		 */
		@Test
		void doesNotForcePreLoading() {
			assertThat(context.getEnvironment().getProperty("springdoc.pre-loading-enabled")).isNull();
		}

	}

	/**
	 * With the MCP property explicitly enabled, the MCP beans exist; the dashboard still
	 * requires its own flag.
	 *
	 * @author bnasslahsen
	 */
	@Nested
	@SpringBootTest(classes = TestApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=5",
			"spring.main.lazy-initialization=false" })
	class WhenPropertyEnabled {

		/**
		 * The application context.
		 */
		@Autowired
		private ApplicationContext context;

		/**
		 * The MCP beans are registered, the dashboard ones are not.
		 */
		@Test
		void registersMcpButNotDashboard() {
			assertThat(context.getBeanNamesForType(SpringDocAiProperties.class)).isNotEmpty();
			assertThat(context.getBeanNamesForType(OpenApiMcpToolCallbackProvider.class)).isNotEmpty();
			assertThat(context.getBean(SpringDocAiProperties.class).isEnabled()).isTrue();
			assertThat(context.getBeanNamesForType(McpDashboardController.class)).isEmpty();
		}

	}

	/**
	 * With both flags set, the dashboard beans exist too.
	 *
	 * @author bnasslahsen
	 */
	@Nested
	@SpringBootTest(classes = TestApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.dashboard-enabled=true",
			"springdoc.ai.mcp.init-timeout-seconds=5", "spring.main.lazy-initialization=false" })
	class WhenDashboardEnabled {

		/**
		 * The application context.
		 */
		@Autowired
		private ApplicationContext context;

		/**
		 * The dashboard beans are registered.
		 */
		@Test
		void registersDashboard() {
			assertThat(context.getBeanNamesForType(McpDashboardController.class)).isNotEmpty();
			assertThat(context.getBeanNamesForType(McpAuditEventStore.class)).isNotEmpty();
		}

	}

}
