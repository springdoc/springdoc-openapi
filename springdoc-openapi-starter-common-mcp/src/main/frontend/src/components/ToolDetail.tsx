import { useRef } from 'react'
import type { McpToolInfo } from '../types'
import HttpMethodBadge from './HttpMethodBadge'
import ToolForm from './ToolForm'
import CopyAsCurl from './CopyAsCurl'

interface ToolDetailProps {
  tool: McpToolInfo
  onExecute: (toolName: string, args: string) => void
  isExecuting: boolean
}

export default function ToolDetail({ tool, onExecute, isExecuting }: ToolDetailProps) {
  const formDataRef = useRef<Record<string, unknown>>({})

  const handleFormChange = (data: Record<string, unknown>) => {
    formDataRef.current = data
  }

  const handleSubmit = (formData: Record<string, unknown>) => {
    onExecute(tool.name, JSON.stringify(formData))
  }

  return (
    <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
      <div className="p-6 border-b border-gray-200 dark:border-gray-700">
        <div className="flex items-center gap-3 mb-2">
          {tool.httpMethod ? (
            <HttpMethodBadge method={tool.httpMethod} className="text-xs px-2 py-1" />
          ) : (
            <span className="px-2 py-1 text-xs font-bold rounded uppercase tracking-wide bg-purple-100 text-purple-800 dark:bg-purple-900/50 dark:text-purple-300">
              MCP Tool
            </span>
          )}
          <h2 className="text-xl font-semibold">{tool.name}</h2>
        </div>
        {tool.path && (
          <code className="text-sm text-gray-500 dark:text-gray-400 bg-gray-100 dark:bg-gray-900 px-2 py-1 rounded">
            {tool.path}
          </code>
        )}
        <p className="mt-3 text-sm text-gray-600 dark:text-gray-300">{tool.description}</p>
      </div>

      {tool.requiresApproval && (
        <div className="mx-6 mt-6 p-4 bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-700 rounded-lg flex gap-3">
          <svg
            className="h-5 w-5 text-amber-500 flex-shrink-0 mt-0.5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={2}
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"
            />
          </svg>
          <div>
            <p className="text-sm font-medium text-amber-800 dark:text-amber-300">Human Approval Required</p>
            <p className="text-xs text-amber-700 dark:text-amber-400 mt-1">
              This mutating operation ({tool.httpMethod} {tool.path}) requires human approval. When called via MCP,
              the AI agent will receive an approval request instead of executing the HTTP call.
            </p>
          </div>
        </div>
      )}

      <div className="p-6">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-sm font-semibold text-gray-700 dark:text-gray-300 uppercase tracking-wide">Parameters</h3>
          <CopyAsCurl toolName={tool.name} formDataRef={formDataRef} />
        </div>
        <ToolForm
          tool={tool}
          onSubmit={handleSubmit}
          onChange={handleFormChange}
          isExecuting={isExecuting}
        />
      </div>
    </div>
  )
}
