import { useState, useMemo } from 'react'
import type { McpToolInfo } from '../types'
import HttpMethodBadge from './HttpMethodBadge'

interface ToolListProps {
  tools: McpToolInfo[]
  isLoading: boolean
  error: Error | null
  selectedTool: McpToolInfo | null
  onSelect: (tool: McpToolInfo) => void
}

export default function ToolList({ tools, isLoading, error, selectedTool, onSelect }: ToolListProps) {
  const [search, setSearch] = useState('')
  const [collapsedGroups, setCollapsedGroups] = useState<Set<string>>(new Set())

  const filtered = tools.filter(
    (t) =>
      t.name.toLowerCase().includes(search.toLowerCase()) ||
      (t.path ?? '').toLowerCase().includes(search.toLowerCase()) ||
      t.description.toLowerCase().includes(search.toLowerCase()),
  )

  const groupedTools = useMemo(() => {
    const groups = new Map<string, McpToolInfo[]>()
    for (const tool of filtered) {
      const key = tool.group ?? 'Other'
      if (!groups.has(key)) {
        groups.set(key, [])
      }
      groups.get(key)!.push(tool)
    }
    return groups
  }, [filtered])

  const toggleGroup = (group: string) => {
    setCollapsedGroups((prev) => {
      const next = new Set(prev)
      if (next.has(group)) {
        next.delete(group)
      } else {
        next.add(group)
      }
      return next
    })
  }

  return (
    <aside className="w-72 flex-shrink-0 border-r border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 flex flex-col overflow-hidden">
      <div className="p-3">
        <input
          type="text"
          placeholder="Search tools..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full px-3 py-2 text-sm border border-gray-300 dark:border-gray-600 rounded-lg bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <div className="flex-1 overflow-y-auto">
        {isLoading && (
          <div className="p-4 text-center text-gray-400">
            <div className="animate-spin h-6 w-6 border-2 border-blue-500 border-t-transparent rounded-full mx-auto mb-2" />
            Loading tools...
          </div>
        )}

        {error && (
          <div className="p-4 text-center text-red-500 text-sm">
            Failed to load tools: {error.message}
          </div>
        )}

        {!isLoading && !error && filtered.length === 0 && (
          <div className="p-4 text-center text-gray-400 text-sm">
            {search ? 'No matching tools' : 'No tools available'}
          </div>
        )}

        {Array.from(groupedTools.entries()).map(([group, groupTools]) => {
          const isCollapsed = collapsedGroups.has(group)
          return (
            <div key={group}>
              <button
                onClick={() => toggleGroup(group)}
                className="w-full flex items-center justify-between px-4 py-2 bg-gray-50 dark:bg-gray-900 border-b border-gray-200 dark:border-gray-700 hover:bg-gray-100 dark:hover:bg-gray-850 transition-colors"
              >
                <div className="flex items-center gap-2">
                  <svg
                    className={`w-3.5 h-3.5 text-gray-500 dark:text-gray-400 transition-transform ${isCollapsed ? '' : 'rotate-90'}`}
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    strokeWidth={2}
                  >
                    <path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" />
                  </svg>
                  <span className="text-xs font-semibold text-gray-600 dark:text-gray-300 uppercase tracking-wide">
                    {group}
                  </span>
                </div>
                <span className="text-[10px] font-medium text-gray-400 dark:text-gray-500 bg-gray-200 dark:bg-gray-700 rounded-full px-1.5 py-0.5">
                  {groupTools.length}
                </span>
              </button>
              {!isCollapsed &&
                groupTools.map((tool) => (
                  <button
                    key={tool.name}
                    onClick={() => onSelect(tool)}
                    className={`w-full text-left px-4 py-3 border-b border-gray-100 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-750 transition-colors ${
                      selectedTool?.name === tool.name
                        ? 'bg-blue-50 dark:bg-blue-900/30 border-l-2 border-l-blue-500'
                        : ''
                    }`}
                  >
                    <div className="flex items-center gap-2 mb-1">
                      {tool.httpMethod && <HttpMethodBadge method={tool.httpMethod} />}
                      {!tool.httpMethod && (
                        <span className="px-1.5 py-0.5 text-[10px] font-bold rounded uppercase tracking-wide bg-purple-100 text-purple-800 dark:bg-purple-900/50 dark:text-purple-300">
                          MCP
                        </span>
                      )}
                      <span className="text-sm font-medium truncate">{tool.name}</span>
                      {tool.requiresApproval && (
                        <svg
                          className="h-3.5 w-3.5 text-amber-500 flex-shrink-0"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                          strokeWidth={2}
                          aria-label="Requires human approval"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"
                          />
                        </svg>
                      )}
                    </div>
                    {tool.path && <div className="text-xs text-gray-500 dark:text-gray-400 truncate">{tool.path}</div>}
                  </button>
                ))}
            </div>
          )
        })}
      </div>
    </aside>
  )
}
