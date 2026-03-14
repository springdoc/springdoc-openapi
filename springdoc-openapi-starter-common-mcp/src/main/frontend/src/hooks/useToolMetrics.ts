import { useState, useCallback } from 'react'
import type { GlobalMetrics, ToolExecution, ToolMetrics, McpToolExecutionResponse } from '../types'

const STORAGE_KEY = 'mcp-tool-metrics'
const MAX_RECENT = 200
const MAX_SPARKLINE = 30

function emptyMetrics(): GlobalMetrics {
  return {
    totalExecutions: 0,
    successCount: 0,
    errorCount: 0,
    approvalCount: 0,
    totalDurationMs: 0,
    recentExecutions: [],
    toolMetrics: {},
  }
}

function loadMetrics(): GlobalMetrics {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? (JSON.parse(raw) as GlobalMetrics) : emptyMetrics()
  } catch {
    return emptyMetrics()
  }
}

function saveMetrics(m: GlobalMetrics) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(m))
  } catch {
    // quota exceeded — ignore
  }
}

export function useToolMetrics() {
  const [metrics, setMetrics] = useState<GlobalMetrics>(loadMetrics)

  const recordExecution = useCallback((toolName: string, response: McpToolExecutionResponse) => {
    const execution: ToolExecution = {
      id: `${Date.now()}-${Math.random().toString(36).slice(2, 7)}`,
      toolName,
      timestamp: Date.now(),
      durationMs: response.durationMs ?? 0,
      success: response.success && !response.requiresApproval,
      httpStatusCode: response.httpStatusCode,
      requiresApproval: response.requiresApproval,
    }

    setMetrics((prev) => {
      const next = { ...prev }
      next.totalExecutions += 1
      next.totalDurationMs += execution.durationMs

      if (execution.requiresApproval) {
        next.approvalCount += 1
      } else if (execution.success) {
        next.successCount += 1
      } else {
        next.errorCount += 1
      }

      next.recentExecutions = [execution, ...prev.recentExecutions].slice(0, MAX_RECENT)

      const tm: ToolMetrics = prev.toolMetrics[toolName] ?? {
        toolName,
        totalCalls: 0,
        successCount: 0,
        errorCount: 0,
        approvalCount: 0,
        totalDurationMs: 0,
        recentDurations: [],
      }

      const updatedTm: ToolMetrics = {
        ...tm,
        totalCalls: tm.totalCalls + 1,
        successCount: tm.successCount + (execution.success ? 1 : 0),
        errorCount: tm.errorCount + (!execution.success && !execution.requiresApproval ? 1 : 0),
        approvalCount: tm.approvalCount + (execution.requiresApproval ? 1 : 0),
        totalDurationMs: tm.totalDurationMs + execution.durationMs,
        recentDurations: [execution.durationMs, ...tm.recentDurations].slice(0, MAX_SPARKLINE),
        lastExecution: execution.timestamp,
      }

      next.toolMetrics = { ...prev.toolMetrics, [toolName]: updatedTm }
      saveMetrics(next)
      return next
    })
  }, [])

  const clearMetrics = useCallback(() => {
    const fresh = emptyMetrics()
    saveMetrics(fresh)
    setMetrics(fresh)
  }, [])

  return { metrics, recordExecution, clearMetrics }
}
