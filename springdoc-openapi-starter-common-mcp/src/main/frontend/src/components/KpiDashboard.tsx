import type { GlobalMetrics, ToolMetrics, McpToolInfo } from '../types'
import Sparkline from './Sparkline'

interface KpiDashboardProps {
  metrics: GlobalMetrics
  totalTools: number
  tools: McpToolInfo[]
  onClear: () => void
}

// ── helpers ──────────────────────────────────────────────────────────────────

function pct(num: number, den: number) {
  return den === 0 ? 0 : Math.round((num / den) * 100)
}

function avgMs(total: number, count: number) {
  return count === 0 ? 0 : Math.round(total / count)
}

function timeAgo(ts: number) {
  const s = Math.floor((Date.now() - ts) / 1000)
  if (s < 60) return `${s}s ago`
  if (s < 3600) return `${Math.floor(s / 60)}m ago`
  if (s < 86400) return `${Math.floor(s / 3600)}h ago`
  return `${Math.floor(s / 86400)}d ago`
}

function healthColor(successPct: number) {
  if (successPct >= 95) return { dot: 'bg-emerald-500', text: 'text-emerald-600 dark:text-emerald-400', label: 'Healthy' }
  if (successPct >= 80) return { dot: 'bg-amber-400', text: 'text-amber-600 dark:text-amber-400', label: 'Degraded' }
  return { dot: 'bg-red-500', text: 'text-red-600 dark:text-red-400', label: 'Unhealthy' }
}

// ── KPI card ─────────────────────────────────────────────────────────────────

interface CardProps {
  label: string
  value: string | number
  sub?: string
  gradient: string
  icon: React.ReactNode
  sparkData?: number[]
  sparkColor?: string
}

function KpiCard({ label, value, sub, gradient, icon, sparkData }: CardProps) {
  return (
    <div className={`relative overflow-hidden rounded-2xl p-5 ${gradient} shadow-lg`}>
      <div className="flex items-start justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-widest text-white/70">{label}</p>
          <p className="mt-1 text-3xl font-bold text-white">{value}</p>
          {sub && <p className="mt-0.5 text-xs text-white/60">{sub}</p>}
        </div>
        <div className="rounded-xl bg-white/20 p-2.5">{icon}</div>
      </div>
      {sparkData && sparkData.length > 1 && (
        <div className="mt-3 opacity-80">
          <Sparkline data={sparkData} width={120} height={32} color="rgba(255,255,255,0.9)" fill="rgba(255,255,255,0.4)" />
        </div>
      )}
    </div>
  )
}

// ── Tool health row ───────────────────────────────────────────────────────────

function ToolHealthRow({ tm }: { tm: ToolMetrics }) {
  const sp = pct(tm.successCount, tm.totalCalls)
  const h = healthColor(tm.approvalCount === tm.totalCalls ? 100 : sp)

  return (
    <tr className="border-b border-gray-100 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700/40 transition-colors">
      <td className="py-2.5 px-4 font-mono text-xs text-gray-700 dark:text-gray-300 max-w-[160px] truncate">{tm.toolName}</td>
      <td className="py-2.5 px-4 text-center text-sm font-semibold text-gray-800 dark:text-gray-200">{tm.totalCalls}</td>
      <td className="py-2.5 px-4 text-center">
        <span className={`text-sm font-semibold ${sp >= 95 ? 'text-emerald-600 dark:text-emerald-400' : sp >= 80 ? 'text-amber-600 dark:text-amber-400' : 'text-red-600 dark:text-red-400'}`}>
          {tm.approvalCount === tm.totalCalls ? '—' : `${sp}%`}
        </span>
      </td>
      <td className="py-2.5 px-4 text-center text-sm text-gray-600 dark:text-gray-400">
        {avgMs(tm.totalDurationMs, tm.totalCalls)} ms
      </td>
      <td className="py-2.5 px-4 text-center">
        {tm.recentDurations.length > 1 ? (
          <Sparkline data={tm.recentDurations} width={64} height={22} color="#6366f1" fill="#6366f1" />
        ) : (
          <span className="text-xs text-gray-400">—</span>
        )}
      </td>
      <td className="py-2.5 px-4 text-center text-xs text-gray-500 dark:text-gray-400">
        {tm.lastExecution ? timeAgo(tm.lastExecution) : '—'}
      </td>
      <td className="py-2.5 px-4">
        <div className="flex items-center gap-1.5 justify-center">
          <span className={`h-2 w-2 rounded-full ${h.dot} ${sp >= 95 && tm.approvalCount !== tm.totalCalls ? 'animate-pulse' : ''}`} />
          <span className={`text-xs font-medium ${h.text}`}>{tm.approvalCount === tm.totalCalls ? 'HITL Only' : h.label}</span>
        </div>
      </td>
    </tr>
  )
}

// ── Recent execution row ──────────────────────────────────────────────────────

function statusBadge(success: boolean, requiresApproval?: boolean) {
  if (requiresApproval) return <span className="px-1.5 py-0.5 rounded text-[10px] font-semibold bg-amber-100 dark:bg-amber-900/40 text-amber-700 dark:text-amber-300">HITL</span>
  if (success) return <span className="px-1.5 py-0.5 rounded text-[10px] font-semibold bg-emerald-100 dark:bg-emerald-900/40 text-emerald-700 dark:text-emerald-300">OK</span>
  return <span className="px-1.5 py-0.5 rounded text-[10px] font-semibold bg-red-100 dark:bg-red-900/40 text-red-700 dark:text-red-300">ERR</span>
}

// ── SVG icons (inline, no dep) ────────────────────────────────────────────────

const IconZap = () => (
  <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M13 10V3L4 14h7v7l9-11h-7z" />
  </svg>
)
const IconCheck = () => (
  <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
  </svg>
)
const IconClock = () => (
  <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
  </svg>
)
const IconShield = () => (
  <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
  </svg>
)
const IconXCircle = () => (
  <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
  </svg>
)
const IconCpu = () => (
  <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 3H7a2 2 0 00-2 2v2M9 3h6M9 3v2m6-2h2a2 2 0 012 2v2m0 0h2m-2 0v6m0 0h2m-2 0v2a2 2 0 01-2 2h-2m0 0H9m6 0v2m-6-2H7a2 2 0 01-2-2v-2m0 0H3m2 0V9M3 9h2m0 0V7" />
  </svg>
)

// ── Read vs Write Pie Chart ───────────────────────────────────────────────────

const SAFE_METHODS = new Set(['GET', 'HEAD', 'OPTIONS'])

interface ReadWriteData {
  readCount: number
  writeCount: number
}

function computeReadWriteRatio(metrics: GlobalMetrics, tools: McpToolInfo[]): ReadWriteData {
  const toolSafeMap = new Map<string, boolean>()
  for (const t of tools) {
    const isSafe = t.safe === true || (t.httpMethod != null && SAFE_METHODS.has(t.httpMethod.toUpperCase()))
    toolSafeMap.set(t.name, isSafe)
  }

  let readCount = 0
  let writeCount = 0
  for (const exec of metrics.recentExecutions) {
    const isSafe = toolSafeMap.get(exec.toolName)
    if (isSafe === true) readCount++
    else if (isSafe === false) writeCount++
    // tools not found in the map are skipped
  }
  return { readCount, writeCount }
}

function ReadWritePieChart({ readCount, writeCount }: ReadWriteData) {
  const total = readCount + writeCount
  if (total === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-8 text-gray-400 dark:text-gray-500">
        <svg className="h-10 w-10 mb-3 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M11 3.055A9.001 9.001 0 1020.945 13H11V3.055z" />
          <path strokeLinecap="round" strokeLinejoin="round" d="M20.488 9H15V3.512A9.025 9.025 0 0120.488 9z" />
        </svg>
        <p className="text-sm">No executions yet</p>
      </div>
    )
  }

  const readPct = readCount / total
  const writePct = writeCount / total

  // SVG donut chart
  const size = 140
  const cx = size / 2
  const cy = size / 2
  const r = 52
  const strokeWidth = 24
  const circumference = 2 * Math.PI * r

  const readLen = circumference * readPct
  const writeLen = circumference * writePct

  return (
    <div className="flex items-center gap-6 py-2">
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} className="flex-shrink-0">
        {/* Write arc (bottom layer) */}
        <circle
          cx={cx} cy={cy} r={r}
          fill="none"
          stroke="#f97316"
          strokeWidth={strokeWidth}
          strokeDasharray={`${writeLen} ${circumference}`}
          strokeDashoffset={-readLen}
          strokeLinecap="round"
          transform={`rotate(-90 ${cx} ${cy})`}
          className="transition-all duration-500"
        />
        {/* Read arc (top layer) */}
        <circle
          cx={cx} cy={cy} r={r}
          fill="none"
          stroke="#10b981"
          strokeWidth={strokeWidth}
          strokeDasharray={`${readLen} ${circumference}`}
          strokeLinecap="round"
          transform={`rotate(-90 ${cx} ${cy})`}
          className="transition-all duration-500"
        />
        {/* Center label */}
        <text x={cx} y={cy - 6} textAnchor="middle" className="fill-gray-700 dark:fill-gray-200 text-lg font-bold" fontSize="20">{total}</text>
        <text x={cx} y={cy + 12} textAnchor="middle" className="fill-gray-400 dark:fill-gray-500 text-xs" fontSize="11">calls</text>
      </svg>
      <div className="space-y-3">
        <div className="flex items-center gap-2.5">
          <span className="h-3 w-3 rounded-full bg-emerald-500" />
          <div>
            <p className="text-sm font-semibold text-gray-700 dark:text-gray-200">Read (Safe)</p>
            <p className="text-xs text-gray-400">{readCount} call{readCount !== 1 ? 's' : ''} · {Math.round(readPct * 100)}%</p>
          </div>
        </div>
        <div className="flex items-center gap-2.5">
          <span className="h-3 w-3 rounded-full bg-orange-500" />
          <div>
            <p className="text-sm font-semibold text-gray-700 dark:text-gray-200">Write (Mutating)</p>
            <p className="text-xs text-gray-400">{writeCount} call{writeCount !== 1 ? 's' : ''} · {Math.round(writePct * 100)}%</p>
          </div>
        </div>
      </div>
    </div>
  )
}

// ── Main Dashboard ────────────────────────────────────────────────────────────

export default function KpiDashboard({ metrics, totalTools, tools, onClear }: KpiDashboardProps) {
  const { totalExecutions, successCount, errorCount, approvalCount, totalDurationMs, recentExecutions, toolMetrics } = metrics

  const successRate = pct(successCount, totalExecutions - approvalCount || 1)
  const avgDuration = avgMs(totalDurationMs, totalExecutions)
  const activeTools = Object.keys(toolMetrics).length

  const globalDurations = recentExecutions.map((e) => e.durationMs)
  const toolList = Object.values(toolMetrics).sort((a, b) => b.totalCalls - a.totalCalls)
  const readWrite = computeReadWriteRatio(metrics, tools)

  const todayStart = new Date().setHours(0, 0, 0, 0)
  const callsToday = recentExecutions.filter((e) => e.timestamp >= todayStart).length

  return (
    <div className="flex-1 overflow-y-auto p-6 space-y-6">

      {/* ── KPI cards ── */}
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-3 xl:grid-cols-6">
        <KpiCard
          label="Total Executions"
          value={totalExecutions.toLocaleString()}
          sub={`${callsToday} today`}
          gradient="bg-gradient-to-br from-indigo-500 to-indigo-700"
          icon={<IconZap />}
          sparkData={globalDurations}
          sparkColor="rgba(255,255,255,0.9)"
        />
        <KpiCard
          label="Success Rate"
          value={totalExecutions === 0 ? '—' : `${successRate}%`}
          sub={`${successCount} successful`}
          gradient="bg-gradient-to-br from-emerald-500 to-emerald-700"
          icon={<IconCheck />}
        />
        <KpiCard
          label="Avg Response"
          value={totalExecutions === 0 ? '—' : `${avgDuration} ms`}
          sub="per tool call"
          gradient="bg-gradient-to-br from-sky-500 to-sky-700"
          icon={<IconClock />}
          sparkData={globalDurations}
          sparkColor="rgba(255,255,255,0.9)"
        />
        <KpiCard
          label="HITL Blocked"
          value={approvalCount.toLocaleString()}
          sub="approval required"
          gradient="bg-gradient-to-br from-amber-500 to-orange-600"
          icon={<IconShield />}
        />
        <KpiCard
          label="Errors"
          value={errorCount.toLocaleString()}
          sub={totalExecutions > 0 ? `${pct(errorCount, totalExecutions)}% error rate` : 'no calls yet'}
          gradient="bg-gradient-to-br from-red-500 to-rose-700"
          icon={<IconXCircle />}
        />
        <KpiCard
          label="Active Tools"
          value={`${activeTools} / ${totalTools}`}
          sub="seen in this session"
          gradient="bg-gradient-to-br from-violet-500 to-purple-700"
          icon={<IconCpu />}
        />
      </div>

      {/* ── Bottom section: health table + activity feed + pie chart ── */}
      <div className="grid grid-cols-1 gap-6 xl:grid-cols-4">

        {/* Tool Health Table */}
        <div className="xl:col-span-2 rounded-2xl bg-white dark:bg-gray-800 shadow-md overflow-hidden">
          <div className="flex items-center justify-between px-5 py-4 border-b border-gray-100 dark:border-gray-700">
            <h2 className="text-sm font-semibold text-gray-700 dark:text-gray-200">Tool Health</h2>
            <span className="text-xs text-gray-400">{toolList.length} tools tracked</span>
          </div>
          {toolList.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-16 text-gray-400 dark:text-gray-500">
              <svg className="h-10 w-10 mb-3 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <p className="text-sm">No executions yet. Run some tools to see health stats.</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left">
                <thead>
                  <tr className="text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider bg-gray-50 dark:bg-gray-700/50">
                    <th className="py-2 px-4">Tool</th>
                    <th className="py-2 px-4 text-center">Calls</th>
                    <th className="py-2 px-4 text-center">Success</th>
                    <th className="py-2 px-4 text-center">Avg ms</th>
                    <th className="py-2 px-4 text-center">Trend</th>
                    <th className="py-2 px-4 text-center">Last Active</th>
                    <th className="py-2 px-4 text-center">Health</th>
                  </tr>
                </thead>
                <tbody>
                  {toolList.map((tm) => (
                    <ToolHealthRow key={tm.toolName} tm={tm} />
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Recent Activity Feed */}
        <div className="rounded-2xl bg-white dark:bg-gray-800 shadow-md overflow-hidden">
          <div className="flex items-center justify-between px-5 py-4 border-b border-gray-100 dark:border-gray-700">
            <h2 className="text-sm font-semibold text-gray-700 dark:text-gray-200">Recent Activity</h2>
            {totalExecutions > 0 && (
              <button
                onClick={onClear}
                className="text-xs text-gray-400 hover:text-red-500 dark:hover:text-red-400 transition-colors"
              >
                Clear
              </button>
            )}
          </div>
          <div className="overflow-y-auto max-h-[420px]">
            {recentExecutions.length === 0 ? (
              <div className="flex flex-col items-center justify-center py-16 text-gray-400 dark:text-gray-500">
                <svg className="h-10 w-10 mb-3 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                  <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p className="text-sm">No activity yet</p>
              </div>
            ) : (
              <ul className="divide-y divide-gray-100 dark:divide-gray-700">
                {recentExecutions.slice(0, 50).map((ex) => (
                  <li key={ex.id} className="flex items-center gap-3 px-4 py-2.5 hover:bg-gray-50 dark:hover:bg-gray-700/40 transition-colors">
                    <div className="flex-shrink-0">{statusBadge(ex.success, ex.requiresApproval)}</div>
                    <div className="flex-1 min-w-0">
                      <p className="text-xs font-mono text-gray-700 dark:text-gray-300 truncate">{ex.toolName}</p>
                      <p className="text-[10px] text-gray-400">{timeAgo(ex.timestamp)}</p>
                    </div>
                    <div className="flex-shrink-0 text-right">
                      <p className="text-xs text-gray-500 dark:text-gray-400">{ex.durationMs} ms</p>
                      {ex.httpStatusCode ? (
                        <p className={`text-[10px] font-medium ${ex.httpStatusCode < 300 ? 'text-emerald-500' : ex.httpStatusCode < 500 ? 'text-amber-500' : 'text-red-500'}`}>
                          HTTP {ex.httpStatusCode}
                        </p>
                      ) : null}
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>

        {/* Read vs. Write Ratio */}
        <div className="rounded-2xl bg-white dark:bg-gray-800 shadow-md overflow-hidden">
          <div className="px-5 py-4 border-b border-gray-100 dark:border-gray-700">
            <h2 className="text-sm font-semibold text-gray-700 dark:text-gray-200">Read vs. Write Ratio</h2>
            <p className="text-xs text-gray-400 mt-0.5">Safe (GET/HEAD) vs. Mutating (POST/PUT/DELETE)</p>
          </div>
          <div className="flex items-center justify-center px-5 py-4">
            <ReadWritePieChart readCount={readWrite.readCount} writeCount={readWrite.writeCount} />
          </div>
        </div>
      </div>
    </div>
  )
}
