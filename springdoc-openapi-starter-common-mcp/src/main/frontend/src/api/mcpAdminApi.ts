import type { McpToolInfo, McpToolExecutionRequest, McpToolExecutionResponse } from '../types'

function resolveBasePath(): string {
  const { pathname } = window.location
  const mcpUiIndex = pathname.indexOf('/mcp-ui')
  const prefix = mcpUiIndex > 0 ? pathname.substring(0, mcpUiIndex) : ''
  return `${prefix}/api/mcp-admin`
}

const BASE_PATH = resolveBasePath()

export async function fetchTools(): Promise<McpToolInfo[]> {
  const res = await fetch(`${BASE_PATH}/tools`)
  if (!res.ok) throw new Error(`Failed to fetch tools: ${res.statusText}`)
  return res.json()
}

export async function executeTool(
  request: McpToolExecutionRequest,
  authHeaders?: Record<string, string>,
): Promise<McpToolExecutionResponse> {
  const headers: Record<string, string> = { 'Content-Type': 'application/json', ...authHeaders }
  let res: Response
  try {
    res = await fetch(`${BASE_PATH}/tools/execute`, {
      method: 'POST',
      headers,
      body: JSON.stringify(request),
    })
  } catch (err) {
    return { success: false, result: null, error: err instanceof Error ? err.message : String(err), durationMs: 0, httpStatusCode: 0 }
  }

  // Try to parse the body regardless of status code
  const text = await res.text()
  if (res.ok) {
    try {
      return JSON.parse(text) as McpToolExecutionResponse
    } catch {
      return { success: true, result: text, error: null, durationMs: 0, httpStatusCode: res.status }
    }
  }

  // Non-ok response — try to extract a meaningful error message
  try {
    const json = JSON.parse(text)
    // Server may return McpToolExecutionResponse or a Spring error body
    if (json.error !== undefined && json.success !== undefined) {
      return json as McpToolExecutionResponse
    }
    const message = json.message || json.error || json.detail || text
    return { success: false, result: null, error: `${res.status} ${res.statusText}: ${message}`, durationMs: 0, httpStatusCode: res.status }
  } catch {
    const errorMsg = text || `${res.status} ${res.statusText}`
    return { success: false, result: null, error: errorMsg, durationMs: 0, httpStatusCode: res.status }
  }
}

export async function fetchAuditEvents(limit = 200): Promise<unknown[]> {
  const res = await fetch(`${BASE_PATH}/audit?limit=${limit}`)
  if (!res.ok) throw new Error(`Failed to fetch audit events: ${res.statusText}`)
  const raw: string[] = await res.json()
  return raw.map((s) => {
    try { return JSON.parse(s) } catch { return { raw: s } }
  })
}

export async function clearAuditEvents(): Promise<void> {
  await fetch(`${BASE_PATH}/audit`, { method: 'DELETE' })
}
