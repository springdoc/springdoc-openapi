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

package test.org.springdoc.ai.mcp.group;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springdoc.ai.dashboard.McpDashboardController;
import org.springdoc.ai.dashboard.McpToolInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that MCP tools are grouped by their OpenAPI @Tag annotations.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false" })
class McpToolGroupTest {

	/**
	 * The dashboard controller.
	 */
	@Autowired
	private McpDashboardController dashboardController;

	/**
	 * The test application.
	 */
	@SpringBootApplication
	static class TestApp {

	}

	/**
	 * Verifies that tools from UserController are grouped under "Users".
	 */
	@Test
	void userToolsGroupedUnderUsersTag() {
		List<McpToolInfo> tools = dashboardController.listTools();
		Map<String, String> toolGroups = tools.stream()
			.collect(Collectors.toMap(McpToolInfo::getName, t -> t.getGroup() != null ? t.getGroup() : ""));

		assertThat(toolGroups.get("list_users")).isEqualTo("Users");
		assertThat(toolGroups.get("get_user_by_id")).isEqualTo("Users");
	}

	/**
	 * Verifies that tools from ProductController are grouped under "Products".
	 */
	@Test
	void productToolsGroupedUnderProductsTag() {
		List<McpToolInfo> tools = dashboardController.listTools();
		Map<String, String> toolGroups = tools.stream()
			.collect(Collectors.toMap(McpToolInfo::getName, t -> t.getGroup() != null ? t.getGroup() : ""));

		assertThat(toolGroups.get("list_products")).isEqualTo("Products");
		assertThat(toolGroups.get("create_product")).isEqualTo("Products");
	}

}
