/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.ai.mcp.groupswithoutdefault;

import java.util.List;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests that the MCP tools are registered from every API group when the default API docs
 * are disabled, with a single tool for an operation that several groups document.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=5",
		"springdoc.enable-default-api-docs=false", "springdoc.group-configs[0].group=orders",
		"springdoc.group-configs[0].paths-to-match=/orders/**", "springdoc.group-configs[1].group=invoices",
		"springdoc.group-configs[1].paths-to-match=/invoices/**", "springdoc.group-configs[2].group=all",
		"springdoc.group-configs[2].paths-to-match=/**", "spring.main.lazy-initialization=false" })
class McpGroupsWithoutDefaultApiDocsTest {

	/**
	 * The MCP server.
	 */
	@MockitoBean
	private McpSyncServer mcpServer;

	/**
	 * The test application.
	 */
	@SpringBootApplication
	static class TestApp {

	}

	/**
	 * Verifies that each operation of every group is registered once as a tool.
	 */
	@Test
	void toolsRegisteredFromEveryGroup() {
		ArgumentCaptor<McpServerFeatures.SyncToolSpecification> tools = ArgumentCaptor
			.forClass(McpServerFeatures.SyncToolSpecification.class);
		verify(mcpServer, times(4)).addTool(tools.capture());
		verify(mcpServer).notifyToolsListChanged();
		List<String> toolNames = tools.getAllValues().stream().map(tool -> tool.tool().name()).toList();
		assertThat(toolNames).containsExactlyInAnyOrder("list_orders", "get_order", "list_invoices", "get_invoice");
	}

}
