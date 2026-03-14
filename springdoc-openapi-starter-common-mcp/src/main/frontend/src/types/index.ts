export interface McpToolInfo {
  name: string
  description: string
  inputSchema: string
  httpMethod: string | null
  path: string | null
  group: string | null
  safe?: boolean
  requiresApproval?: boolean
}

export interface McpToolExecutionRequest {
  toolName: string
  arguments: string
}

export interface McpToolExecutionResponse {
  success: boolean
  result: string | null
  error: string | null
  durationMs: number
  httpStatusCode: number
  requiresApproval?: boolean
}

export interface ToolExecution {
  id: string
  toolName: string
  timestamp: number
  durationMs: number
  success: boolean
  httpStatusCode?: number
  requiresApproval?: boolean
}

export interface ToolMetrics {
  toolName: string
  totalCalls: number
  successCount: number
  errorCount: number
  approvalCount: number
  totalDurationMs: number
  recentDurations: number[]
  lastExecution?: number
}

export interface GlobalMetrics {
  totalExecutions: number
  successCount: number
  errorCount: number
  approvalCount: number
  totalDurationMs: number
  recentExecutions: ToolExecution[]
  toolMetrics: Record<string, ToolMetrics>
}
