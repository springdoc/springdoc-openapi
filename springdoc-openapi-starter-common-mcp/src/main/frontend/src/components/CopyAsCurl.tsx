import { useState, type RefObject } from 'react'

interface CopyAsCurlProps {
  toolName: string
  formDataRef: RefObject<Record<string, unknown>>
}

export default function CopyAsCurl({ toolName, formDataRef }: CopyAsCurlProps) {
  const [copied, setCopied] = useState(false)

  const handleCopy = () => {
    const args = JSON.stringify(formDataRef.current ?? {})
    const body = JSON.stringify({ toolName, arguments: args })
    const curl = `curl -X POST http://localhost:8080/api/mcp-admin/tools/execute -H 'Content-Type: application/json' -d '${body}'`

    navigator.clipboard.writeText(curl).then(() => {
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    })
  }

  return (
    <button
      onClick={handleCopy}
      className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium text-gray-600 dark:text-gray-400 bg-gray-100 dark:bg-gray-700 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors"
    >
      {copied ? (
        <>
          <svg className="h-3.5 w-3.5 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
          </svg>
          Copied!
        </>
      ) : (
        <>
          <svg className="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M8 5H6a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2v-1M8 5a2 2 0 002 2h2a2 2 0 002-2M8 5a2 2 0 012-2h2a2 2 0 012 2m0 0h2a2 2 0 012 2v3m2 4H10m0 0l3-3m-3 3l3 3" />
          </svg>
          Copy as cURL
        </>
      )}
    </button>
  )
}
