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

package test.org.springdoc.ai.mcp.syncpreferred;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpStatelessServerFeatures;
import io.modelcontextprotocol.server.McpStatelessSyncServer;
import io.modelcontextprotocol.server.McpSyncServer;
import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests that the MCP tools are registered with the {@link McpSyncServer} when it is present,
 * and not with a {@link McpStatelessSyncServer}.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false" })
class McpSyncServerPreferredTest {

	/**
	 * The MCP server.
	 */
	@MockitoBean
	private McpSyncServer mcpServer;

	/**
	 * The stateless MCP server.
	 */
	@MockitoBean
	private McpStatelessSyncServer statelessMcpServer;

	/**
	 * The test application.
	 */
	@SpringBootApplication
	static class TestApp {

	}

	/**
	 * Verifies that the tools are registered with the MCP server only.
	 */
	@Test
	void toolsRegisteredWithSyncServerOnly() {
		verify(mcpServer, times(2)).addTool(any(McpServerFeatures.SyncToolSpecification.class));
		verify(mcpServer).notifyToolsListChanged();
		verify(statelessMcpServer, never()).addTool(any(McpStatelessServerFeatures.SyncToolSpecification.class));
	}

}
