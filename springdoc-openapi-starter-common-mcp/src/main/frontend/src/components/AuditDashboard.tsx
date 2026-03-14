import { useState, useEffect, useRef, useCallback } from 'react'
import { fetchAuditEvents, clearAuditEvents, fetchTools } from '../api/mcpAdminApi'
import type { McpToolInfo } from '../types'
import JsonTreeView from './JsonTreeView'

// ── Types ─────────────────────────────────────────────────────────────────────

interface AuditIdentity {
  principal?: string
  roles?: string[]
  client_ip?: string
}

interface AuditExecution {
  tool_name?: string
  http_method?: string
  path_pattern?: string
  resolved_path?: string
  hitl_status?: string
  duration_ms?: number
  rest_duration_ms?: number
}

interface AuditOutcome {
  status?: string
  http_status_code?: number
  error_reason?: string
}

interface AuditTranslation {
  mcp_arguments?: object
  request_url?: string
  request_body?: object | string
  response_body?: string
  mcp_arguments_size?: number
  request_body_size?: number
  response_body_size?: number
}

interface AuditEvent {
  event_type?: string
  timestamp?: string
  trace_id?: string
  session_id?: string
  identity?: AuditIdentity
  execution?: AuditExecution
  outcome?: AuditOutcome
  translation?: AuditTranslation
}

// ── Helpers ───────────────────────────────────────────────────────────────────

function outcomeBadge(status?: string, hitl?: string) {
  if (hitl === 'INTERCEPTED') {
    return <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-amber-100 dark:bg-amber-900/40 text-amber-700 dark:text-amber-300 uppercase tracking-wide">HITL</span>
  }
  if (status === 'SUCCESS') {
    return <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-emerald-100 dark:bg-emerald-900/40 text-emerald-700 dark:text-emerald-300 uppercase tracking-wide">OK</span>
  }
  if (status === 'APPROVAL_REQUIRED') {
    return <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-amber-100 dark:bg-amber-900/40 text-amber-700 dark:text-amber-300 uppercase tracking-wide">APPROVAL</span>
  }
  return <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-red-100 dark:bg-red-900/40 text-red-700 dark:text-red-300 uppercase tracking-wide">ERROR</span>
}

function methodBadge(method?: string) {
  if (!method) return null
  const colors: Record<string, string> = {
    GET: 'bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-300',
    POST: 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300',
    PUT: 'bg-orange-100 dark:bg-orange-900/30 text-orange-700 dark:text-orange-300',
    PATCH: 'bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-300',
    DELETE: 'bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-300',
  }
  const cls = colors[method] ?? 'bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300'
  return <span className={`px-1.5 py-0.5 rounded text-[10px] font-bold ${cls}`}>{method}</span>
}

function httpStatusColor(code?: number) {
  if (!code) return 'text-gray-400'
  if (code < 300) return 'text-emerald-600 dark:text-emerald-400'
  if (code < 500) return 'text-amber-600 dark:text-amber-400'
  return 'text-red-600 dark:text-red-400'
}

function fmtTs(ts?: string) {
  if (!ts) return '—'
  try {
    const d = new Date(ts)
    return d.toLocaleTimeString(undefined, { hour12: false }) + '.' + String(d.getMilliseconds()).padStart(3, '0')
  } catch { return ts }
}

function estimateTokens(bytes: number): number {
  return Math.ceil(bytes / 4)
}

function formatBytes(bytes: number): string {
  return bytes < 1024 ? `${bytes} B` : `${(bytes / 1024).toFixed(1)} KB`
}

type OutcomeFilter = 'ALL' | 'SUCCESS' | 'ERROR' | 'HITL' | 'APPROVAL_REQUIRED'
type TraceTab = 'intent' | 'request' | 'response'

// ── Schema Diff ───────────────────────────────────────────────────────────────

interface SchemaDiff {
  missing: string[]
  extra: string[]
  typeMismatches: { field: string; expected: string; actual: string }[]
}

function computeSchemaDiff(inputSchema: string | undefined, mcpArgs: object | undefined): SchemaDiff | null {
  if (!inputSchema || !mcpArgs) return null
  try {
    const schema = typeof inputSchema === 'string' ? JSON.parse(inputSchema) : inputSchema
    const props = schema.properties ?? {}
    const required: string[] = schema.required ?? []
    const argKeys = Object.keys(mcpArgs as Record<string, unknown>)
    const schemaKeys = Object.keys(props)

    const missing = required.filter((k: string) => !argKeys.includes(k))
    const extra = argKeys.filter((k) => !schemaKeys.includes(k))

    const typeMismatches: SchemaDiff['typeMismatches'] = []
    for (const key of argKeys) {
      if (!props[key]) continue
      const expected = props[key].type as string | undefined
      if (!expected) continue
      const val = (mcpArgs as Record<string, unknown>)[key]
      const actual = Array.isArray(val) ? 'array' : typeof val
      if (expected !== actual && !(expected === 'integer' && actual === 'number')) {
        typeMismatches.push({ field: key, expected, actual })
      }
    }

    if (missing.length === 0 && extra.length === 0 && typeMismatches.length === 0) return null
    return { missing, extra, typeMismatches }
  } catch {
    return null
  }
}

// ── Latency Bar ──────────────────────────────────────────────────────────────

function LatencyBar({ durationMs, restDurationMs }: { durationMs?: number; restDurationMs?: number }) {
  if (durationMs == null) return <span className="text-xs text-gray-400">—</span>
  if (restDurationMs == null) return <span className="text-xs tabular-nums text-gray-500 dark:text-gray-400">{durationMs} ms</span>

  const overhead = durationMs - restDurationMs
  const restPct = durationMs > 0 ? (restDurationMs / durationMs) * 100 : 0
  const overheadPct = 100 - restPct

  return (
    <div
      className="flex items-center gap-1.5"
      title={`Total: ${durationMs}ms | REST API: ${restDurationMs}ms | Overhead: ${overhead}ms`}
    >
      <div className="w-[80px] h-[6px] rounded-full overflow-hidden flex bg-gray-200 dark:bg-gray-700">
        <div
          className="h-full bg-emerald-500"
          style={{ width: `${restPct}%` }}
        />
        <div
          className="h-full bg-gray-400 dark:bg-gray-500"
          style={{ width: `${overheadPct}%` }}
        />
      </div>
      <span className="text-xs tabular-nums text-gray-500 dark:text-gray-400">{durationMs} ms</span>
    </div>
  )
}

// ── Trace Viewer ──────────────────────────────────────────────────────────────

function TraceViewer({ event, tools }: { event: AuditEvent; tools: McpToolInfo[] }) {
  const [tab, setTab] = useState<TraceTab>('intent')
  const translation = event.translation
  const exec = event.execution ?? {}
  const outcome = event.outcome ?? {}

  const hasTranslation = translation && (translation.mcp_arguments || translation.request_url || translation.response_body)

  if (!hasTranslation) {
    return <MetadataSection event={event} />
  }

  const isError = (outcome.http_status_code ?? 0) >= 400
  const tool = tools.find((t) => t.name === exec.tool_name)
  const diff = isError ? computeSchemaDiff(tool?.inputSchema, translation?.mcp_arguments) : null

  const tabs: { id: TraceTab; label: string }[] = [
    { id: 'intent', label: 'LLM Intent' },
    { id: 'request', label: 'REST Request' },
    { id: 'response', label: 'Response' },
  ]

  return (
    <div className="space-y-3">
      {/* Tab bar */}
      <div className="flex gap-1 border-b border-gray-200 dark:border-gray-700">
        {tabs.map((t) => (
          <button
            key={t.id}
            onClick={() => setTab(t.id)}
            className={`px-3 py-1.5 text-xs font-medium border-b-2 transition-colors ${
              tab === t.id
                ? 'border-indigo-500 text-indigo-600 dark:text-indigo-400'
                : 'border-transparent text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300'
            }`}
          >
            {t.label}
            {t.id === 'response' && isError && (
              <span className="ml-1.5 inline-flex h-4 w-4 items-center justify-center rounded-full bg-red-100 dark:bg-red-900/40 text-red-600 dark:text-red-400 text-[9px] font-bold">!</span>
            )}
          </button>
        ))}
      </div>

      {/* Tab content */}
      <div className="min-h-[80px] max-h-[400px] overflow-auto rounded-lg bg-gray-50 dark:bg-gray-900 p-3">
        {tab === 'intent' && (
          translation?.mcp_arguments ? (
            <JsonTreeView data={translation.mcp_arguments} />
          ) : (
            <span className="text-xs text-gray-400">No MCP arguments captured</span>
          )
        )}

        {tab === 'request' && (
          <div className="space-y-2">
            <div className="flex items-center gap-2 text-xs">
              {methodBadge(exec.http_method)}
              <code className="text-gray-700 dark:text-gray-300 break-all">{translation?.request_url ?? '—'}</code>
            </div>
            {(exec.rest_duration_ms != null || exec.duration_ms != null) && (
              <div className="flex items-center gap-2 mt-1">
                {exec.rest_duration_ms != null && (
                  <span className="inline-flex items-center px-2 py-0.5 rounded text-[10px] font-bold bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-300">REST API: {exec.rest_duration_ms}ms</span>
                )}
                {exec.rest_duration_ms != null && exec.duration_ms != null && (
                  <span className="inline-flex items-center px-2 py-0.5 rounded text-[10px] font-bold bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300">Overhead: {exec.duration_ms - exec.rest_duration_ms}ms</span>
                )}
              </div>
            )}
            {translation?.request_body ? (
              <div className="mt-2">
                <div className="flex items-center gap-2 text-[10px] font-semibold text-gray-400 uppercase mb-1">
                  <span>Request Body</span>
                  {translation.request_body_size != null && translation.request_body_size > 0 && (
                    <span className="normal-case font-normal text-gray-400">{formatBytes(translation.request_body_size)} (~{estimateTokens(translation.request_body_size)} tokens)</span>
                  )}
                </div>
                {typeof translation.request_body === 'object' ? (
                  <JsonTreeView data={translation.request_body} />
                ) : (
                  <pre className="text-xs text-gray-600 dark:text-gray-400 whitespace-pre-wrap">{translation.request_body}</pre>
                )}
              </div>
            ) : (
              <span className="text-xs text-gray-400 block mt-1">No request body</span>
            )}
          </div>
        )}

        {tab === 'response' && (
          <div className="space-y-2">
            {isError && (
              <div className="px-3 py-2 rounded-md bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-xs text-red-700 dark:text-red-300">
                HTTP {outcome.http_status_code} — {outcome.error_reason ?? 'Error response from API'}
              </div>
            )}
            {translation?.response_body_size != null && translation.response_body_size > 0 && (
              <div className="text-[10px] font-semibold text-gray-400 uppercase">
                Response Size: <span className="normal-case font-normal">{formatBytes(translation.response_body_size)} (~{estimateTokens(translation.response_body_size)} tokens)</span>
              </div>
            )}
            {translation?.response_body_size != null && translation.response_body_size > 2048 && (
              <div className="px-3 py-2 rounded-md bg-orange-50 dark:bg-orange-900/20 border border-orange-200 dark:border-orange-800 text-xs text-orange-700 dark:text-orange-300">
                Large response ({formatBytes(translation.response_body_size)}, ~{estimateTokens(translation.response_body_size)} tokens). Consider creating a lightweight DTO to reduce context window usage.
              </div>
            )}
            {translation?.response_body ? (
              (() => {
                try {
                  const parsed = JSON.parse(translation.response_body!)
                  return <JsonTreeView data={parsed} />
                } catch {
                  return <pre className="text-xs text-gray-600 dark:text-gray-400 whitespace-pre-wrap">{translation.response_body}</pre>
                }
              })()
            ) : (
              <span className="text-xs text-gray-400">No response body captured</span>
            )}
            {diff && (
              <div className="mt-3 space-y-1.5">
                <div className="text-[10px] font-semibold text-gray-400 uppercase">Schema Diff</div>
                {diff.missing.map((f) => (
                  <div key={f} className="flex items-center gap-1.5 text-xs">
                    <span className="inline-block w-2 h-2 rounded-full bg-red-500" />
                    <span className="text-red-600 dark:text-red-400">Missing required field: <code className="font-mono">{f}</code></span>
                  </div>
                ))}
                {diff.extra.map((f) => (
                  <div key={f} className="flex items-center gap-1.5 text-xs">
                    <span className="inline-block w-2 h-2 rounded-full bg-orange-500" />
                    <span className="text-orange-600 dark:text-orange-400">Extra field not in schema: <code className="font-mono">{f}</code></span>
                  </div>
                ))}
                {diff.typeMismatches.map((m) => (
                  <div key={m.field} className="flex items-center gap-1.5 text-xs">
                    <span className="inline-block w-2 h-2 rounded-full bg-yellow-500" />
                    <span className="text-yellow-600 dark:text-yellow-400">
                      Type mismatch on <code className="font-mono">{m.field}</code>: expected <code>{m.expected}</code>, got <code>{m.actual}</code>
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>

      {/* Collapsible metadata */}
      <MetadataSection event={event} collapsible />
    </div>
  )
}

// ── Metadata Section ──────────────────────────────────────────────────────────

function MetadataSection({ event, collapsible }: { event: AuditEvent; collapsible?: boolean }) {
  const [open, setOpen] = useState(!collapsible)
  const exec = event.execution ?? {}
  const outcome = event.outcome ?? {}

  const items = [
    event.trace_id && { label: 'Trace ID', value: event.trace_id, mono: true },
    event.session_id && { label: 'Session ID', value: event.session_id, mono: true },
    event.identity?.client_ip && { label: 'Client IP', value: event.identity.client_ip, mono: true },
    (event.identity?.roles?.length ?? 0) > 0 && { label: 'Roles', value: event.identity!.roles!.join(', ') },
    exec.path_pattern && exec.resolved_path && exec.path_pattern !== exec.resolved_path && { label: 'Path Pattern', value: exec.path_pattern, mono: true },
    exec.hitl_status === 'BYPASSED' && { label: 'HITL Status', value: 'BYPASSED (mutating, no approval required)' },
    outcome.error_reason && { label: 'Error Reason', value: outcome.error_reason, error: true },
  ].filter(Boolean) as { label: string; value: string; mono?: boolean; error?: boolean }[]

  if (items.length === 0) return null

  return (
    <div>
      {collapsible && (
        <button
          onClick={() => setOpen((p) => !p)}
          className="flex items-center gap-1 text-[10px] font-semibold text-gray-400 dark:text-gray-500 uppercase tracking-wide hover:text-gray-600 dark:hover:text-gray-300 transition-colors"
        >
          <svg className={`h-3 w-3 transition-transform ${open ? 'rotate-90' : ''}`} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" />
          </svg>
          Metadata
        </button>
      )}
      {open && (
        <div className="grid grid-cols-1 gap-2 text-xs sm:grid-cols-2 xl:grid-cols-3 mt-1">
          {items.map((item) => (
            <Detail
              key={item.label}
              label={item.label}
              value={item.value}
              mono={item.mono}
              className={item.error ? 'col-span-full text-red-600 dark:text-red-400' : undefined}
            />
          ))}
        </div>
      )}
    </div>
  )
}

// ── Expandable row ────────────────────────────────────────────────────────────

function AuditRow({ event, index, tools }: { event: AuditEvent; index: number; tools: McpToolInfo[] }) {
  const [expanded, setExpanded] = useState(false)
  const exec = event.execution ?? {}
  const outcome = event.outcome ?? {}

  return (
    <>
      <tr
        className={`border-b border-gray-100 dark:border-gray-700 cursor-pointer transition-colors ${
          index % 2 === 0 ? 'bg-white dark:bg-gray-800' : 'bg-gray-50/50 dark:bg-gray-800/50'
        } hover:bg-indigo-50/40 dark:hover:bg-indigo-900/10`}
        onClick={() => setExpanded((p) => !p)}
      >
        {/* expand chevron */}
        <td className="py-2 pl-3 pr-1 text-gray-300 dark:text-gray-600 w-4">
          <svg className={`h-3 w-3 transition-transform ${expanded ? 'rotate-90' : ''}`} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" />
          </svg>
        </td>
        <td className="py-2 px-3 text-xs text-gray-500 dark:text-gray-400 tabular-nums whitespace-nowrap">{fmtTs(event.timestamp)}</td>
        <td className="py-2 px-3">{outcomeBadge(outcome.status, exec.hitl_status)}</td>
        <td className="py-2 px-3 font-mono text-xs text-gray-700 dark:text-gray-300 max-w-[180px] truncate">{exec.tool_name ?? '—'}</td>
        <td className="py-2 px-3">{methodBadge(exec.http_method)}</td>
        <td className="py-2 px-3 text-xs text-gray-500 dark:text-gray-400 font-mono max-w-[200px] truncate">{exec.resolved_path ?? exec.path_pattern ?? '—'}</td>
        <td className="py-2 px-3 text-xs text-gray-600 dark:text-gray-400">{event.identity?.principal ?? 'anonymous'}</td>
        <td className="py-2 px-3 text-xs font-mono text-gray-500 dark:text-gray-400 max-w-[120px] truncate" title={event.identity?.client_ip}>
          {event.identity?.client_ip ?? '—'}
        </td>
        <td className="py-2 px-3 text-xs font-mono text-gray-500 dark:text-gray-400 max-w-[120px] truncate" title={event.session_id}>
          {event.session_id ? event.session_id.substring(0, 8) + '…' : '—'}
        </td>
        <td className="py-2 px-3">
          <LatencyBar durationMs={exec.duration_ms} restDurationMs={exec.rest_duration_ms} />
        </td>
        <td className={`py-2 px-3 text-xs font-semibold tabular-nums ${httpStatusColor(outcome.http_status_code)}`}>
          {outcome.http_status_code ?? '—'}
        </td>
      </tr>
      {expanded && (
        <tr className="border-b border-gray-100 dark:border-gray-700 bg-indigo-50/30 dark:bg-indigo-900/10">
          <td colSpan={11} className="px-8 py-3">
            <TraceViewer event={event} tools={tools} />
          </td>
        </tr>
      )}
    </>
  )
}

function Detail({ label, value, mono, className }: { label: string; value: string; mono?: boolean; className?: string }) {
  return (
    <div className={className}>
      <span className="text-gray-400 dark:text-gray-500">{label}: </span>
      <span className={`text-gray-700 dark:text-gray-300 ${mono ? 'font-mono' : ''}`}>{value}</span>
    </div>
  )
}

// ── Main component ────────────────────────────────────────────────────────────

export default function AuditDashboard() {
  const [events, setEvents] = useState<AuditEvent[]>([])
  const [tools, setTools] = useState<McpToolInfo[]>([])
  const [loading, setLoading] = useState(false)
  const [autoRefresh, setAutoRefresh] = useState(true)
  const [search, setSearch] = useState('')
  const [outcomeFilter, setOutcomeFilter] = useState<OutcomeFilter>('ALL')
  const [error, setError] = useState<string | null>(null)
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null)

  const load = useCallback(async () => {
    try {
      const data = await fetchAuditEvents(200) as AuditEvent[]
      setEvents(data)
      setError(null)
    } catch (e) {
      setError(e instanceof Error ? e.message : String(e))
    } finally {
      setLoading(false)
    }
  }, [])

  // Initial load — events + tools
  useEffect(() => {
    setLoading(true)
    load()
    fetchTools().then(setTools).catch(() => {})
  }, [load])

  // Auto-refresh
  useEffect(() => {
    if (autoRefresh) {
      intervalRef.current = setInterval(load, 3000)
    } else if (intervalRef.current) {
      clearInterval(intervalRef.current)
    }
    return () => { if (intervalRef.current) clearInterval(intervalRef.current) }
  }, [autoRefresh, load])

  const handleClear = async () => {
    await clearAuditEvents()
    setEvents([])
  }

  // Filter
  const filtered = events.filter((e) => {
    const exec = e.execution ?? {}
    const outcome = e.outcome ?? {}

    if (outcomeFilter !== 'ALL') {
      if (outcomeFilter === 'HITL' && exec.hitl_status !== 'INTERCEPTED') return false
      if (outcomeFilter === 'SUCCESS' && (outcome.status !== 'SUCCESS' || exec.hitl_status === 'INTERCEPTED')) return false
      if (outcomeFilter === 'ERROR' && outcome.status !== 'ERROR') return false
      if (outcomeFilter === 'APPROVAL_REQUIRED' && outcome.status !== 'APPROVAL_REQUIRED') return false
    }

    if (search) {
      const q = search.toLowerCase()
      return (
        exec.tool_name?.toLowerCase().includes(q) ||
        exec.path_pattern?.toLowerCase().includes(q) ||
        exec.resolved_path?.toLowerCase().includes(q) ||
        e.identity?.principal?.toLowerCase().includes(q) ||
        outcome.error_reason?.toLowerCase().includes(q) ||
        false
      )
    }
    return true
  })

  const counts = {
    success: events.filter((e) => e.outcome?.status === 'SUCCESS' && e.execution?.hitl_status !== 'INTERCEPTED').length,
    error: events.filter((e) => e.outcome?.status === 'ERROR').length,
    hitl: events.filter((e) => e.execution?.hitl_status === 'INTERCEPTED').length,
    approval: events.filter((e) => e.outcome?.status === 'APPROVAL_REQUIRED').length,
  }

  return (
    <div className="flex-1 overflow-hidden flex flex-col">
      {/* ── Toolbar ── */}
      <div className="flex flex-wrap items-center gap-3 px-6 py-3 bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700">
        {/* Search */}
        <div className="relative flex-1 min-w-[200px] max-w-xs">
          <svg className="absolute left-2.5 top-2 h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            type="text"
            placeholder="Search tool, path, principal…"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-8 pr-3 py-1.5 text-sm rounded-lg border border-gray-200 dark:border-gray-600 bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-400"
          />
        </div>

        {/* Outcome filter pills */}
        <div className="flex items-center gap-1 text-xs">
          {(['ALL', 'SUCCESS', 'ERROR', 'HITL', 'APPROVAL_REQUIRED'] as OutcomeFilter[]).map((f) => (
            <button
              key={f}
              onClick={() => setOutcomeFilter(f)}
              className={`px-2.5 py-1 rounded-full font-semibold transition-colors ${
                outcomeFilter === f
                  ? 'bg-indigo-600 text-white'
                  : 'bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600'
              }`}
            >
              {f === 'APPROVAL_REQUIRED' ? 'APPROVAL' : f}
              {f !== 'ALL' && (
                <span className="ml-1 opacity-70">
                  {f === 'SUCCESS' ? counts.success : f === 'ERROR' ? counts.error : f === 'HITL' ? counts.hitl : counts.approval}
                </span>
              )}
            </button>
          ))}
        </div>

        <div className="flex items-center gap-2 ml-auto">
          {/* Event count */}
          <span className="text-xs text-gray-400">{filtered.length} / {events.length} events</span>

          {/* Auto-refresh toggle */}
          <button
            onClick={() => setAutoRefresh((p) => !p)}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium transition-colors ${
              autoRefresh
                ? 'bg-indigo-100 dark:bg-indigo-900/40 text-indigo-700 dark:text-indigo-300'
                : 'bg-gray-100 dark:bg-gray-700 text-gray-500 dark:text-gray-400'
            }`}
          >
            <span className={`h-1.5 w-1.5 rounded-full ${autoRefresh ? 'bg-indigo-500 animate-pulse' : 'bg-gray-400'}`} />
            {autoRefresh ? 'Live' : 'Paused'}
          </button>

          {/* Refresh now */}
          <button
            onClick={load}
            disabled={loading}
            className="p-1.5 rounded-lg text-gray-500 hover:bg-gray-100 dark:hover:bg-gray-700 disabled:opacity-50 transition-colors"
            title="Refresh now"
          >
            <svg className={`h-4 w-4 ${loading ? 'animate-spin' : ''}`} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
          </button>

          {/* Clear */}
          <button
            onClick={handleClear}
            className="px-3 py-1.5 rounded-lg text-xs font-medium text-red-500 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
          >
            Clear
          </button>
        </div>
      </div>

      {/* ── Table ── */}
      {error ? (
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center text-red-500 dark:text-red-400">
            <p className="font-medium">Failed to load audit events</p>
            <p className="text-sm mt-1 text-gray-500">{error}</p>
          </div>
        </div>
      ) : filtered.length === 0 ? (
        <div className="flex-1 flex items-center justify-center text-gray-400 dark:text-gray-500">
          <div className="text-center">
            <svg className="mx-auto h-12 w-12 mb-4 opacity-40" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            <p className="text-sm font-medium">{events.length === 0 ? 'No audit events yet' : 'No events match the current filter'}</p>
            <p className="text-xs mt-1">{events.length === 0 ? 'Execute some tools to see audit trails here' : 'Try a different search or filter'}</p>
          </div>
        </div>
      ) : (
        <div className="flex-1 overflow-auto">
          <table className="w-full text-left">
            <thead className="sticky top-0 z-10">
              <tr className="text-[10px] font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider bg-gray-50 dark:bg-gray-900 border-b border-gray-200 dark:border-gray-700">
                <th className="py-2 pl-3 pr-1 w-4" />
                <th className="py-2 px-3 whitespace-nowrap">Time</th>
                <th className="py-2 px-3">Status</th>
                <th className="py-2 px-3">Tool</th>
                <th className="py-2 px-3">Method</th>
                <th className="py-2 px-3">Path</th>
                <th className="py-2 px-3">Principal</th>
                <th className="py-2 px-3 whitespace-nowrap">Client IP</th>
                <th className="py-2 px-3 whitespace-nowrap">Session ID</th>
                <th className="py-2 px-3 whitespace-nowrap">Duration</th>
                <th className="py-2 px-3 whitespace-nowrap">HTTP</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((event, i) => (
                <AuditRow key={`${event.timestamp}-${i}`} event={event} index={i} tools={tools} />
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
