import { useMutation } from '@tanstack/react-query'
import { executeTool } from '../api/mcpAdminApi'
import type { McpToolExecutionRequest } from '../types'

interface ExecuteToolParams {
  request: McpToolExecutionRequest
  authHeaders?: Record<string, string>
}

export function useToolExecution() {
  return useMutation({
    mutationFn: ({ request, authHeaders }: ExecuteToolParams) =>
      executeTool(request, authHeaders),
  })
}
