import { useState, useMemo } from 'react'
import type { McpToolExecutionResponse } from '../types'
import JsonTreeView from './JsonTreeView'

interface ResponseViewerProps {
  response: McpToolExecutionResponse
}

export default function ResponseViewer({ response }: ResponseViewerProps) {
  const [tab, setTab] = useState<'pretty' | 'raw'>('pretty')

  const parsedResult = useMemo(() => {
    if (!response.result) return null
    try {
      return JSON.parse(response.result)
    }
    catch {
      return null
    }
  }, [response.result])

  const displayText = response.success ? response.result : response.error

  return (
    <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
      <div className="flex items-center justify-between px-6 py-3 border-b border-gray-200 dark:border-gray-700">
        <div className="flex items-center gap-3">
          <h3 className="text-sm font-semibold text-gray-700 dark:text-gray-300 uppercase tracking-wide">Response</h3>
          {response.httpStatusCode > 0 && (
            <span
              className={`px-2 py-0.5 text-xs font-bold font-mono rounded ${
                response.httpStatusCode >= 200 && response.httpStatusCode < 300
                  ? 'bg-green-100 text-green-800 dark:bg-green-900/50 dark:text-green-300'
                  : response.httpStatusCode >= 400
                    ? 'bg-red-100 text-red-800 dark:bg-red-900/50 dark:text-red-300'
                    : 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/50 dark:text-yellow-300'
              }`}
            >
              {response.httpStatusCode}
            </span>
          )}
          <span
            className={`px-2 py-0.5 text-xs font-medium rounded-full ${
              response.requiresApproval
                ? 'bg-amber-100 text-amber-800 dark:bg-amber-900/50 dark:text-amber-300'
                : response.success
                  ? 'bg-green-100 text-green-800 dark:bg-green-900/50 dark:text-green-300'
                  : 'bg-red-100 text-red-800 dark:bg-red-900/50 dark:text-red-300'
            }`}
          >
            {response.requiresApproval ? 'Approval Required' : response.success ? 'Success' : 'Error'}
          </span>
          <span className="text-xs text-gray-400">{response.durationMs}ms</span>
        </div>

        {parsedResult && (
          <div className="flex bg-gray-100 dark:bg-gray-700 rounded-lg p-0.5">
            <button
              onClick={() => setTab('pretty')}
              className={`px-3 py-1 text-xs font-medium rounded-md transition-colors ${
                tab === 'pretty'
                  ? 'bg-white dark:bg-gray-600 shadow-sm text-gray-900 dark:text-gray-100'
                  : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-200'
              }`}
            >
              Pretty
            </button>
            <button
              onClick={() => setTab('raw')}
              className={`px-3 py-1 text-xs font-medium rounded-md transition-colors ${
                tab === 'raw'
                  ? 'bg-white dark:bg-gray-600 shadow-sm text-gray-900 dark:text-gray-100'
                  : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-200'
              }`}
            >
              Raw
            </button>
          </div>
        )}
      </div>

      <div className="p-6 max-h-96 overflow-auto">
        {response.requiresApproval ? (
          <div className="flex gap-3 p-4 bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-700 rounded-lg">
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
              <p className="text-xs text-amber-700 dark:text-amber-400 mt-1">{response.error}</p>
            </div>
          </div>
        ) : !response.success ? (
          <pre className="text-sm text-red-500 font-mono whitespace-pre-wrap break-all">{displayText}</pre>
        ) : parsedResult && tab === 'pretty' ? (
          <JsonTreeView data={parsedResult} />
        ) : (
          <pre className="text-sm font-mono text-gray-800 dark:text-gray-200 whitespace-pre-wrap break-all bg-gray-50 dark:bg-gray-900 p-4 rounded-lg">
            {displayText}
          </pre>
        )}
      </div>
    </div>
  )
}
