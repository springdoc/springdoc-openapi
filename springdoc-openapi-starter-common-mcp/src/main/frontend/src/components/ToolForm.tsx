import { useMemo } from 'react'
import Form from '@rjsf/core'
import type { IChangeEvent } from '@rjsf/core'
import validator from '@rjsf/validator-ajv8'
import type { RJSFSchema } from '@rjsf/utils'
import type { McpToolInfo } from '../types'

interface ToolFormProps {
  tool: McpToolInfo
  onSubmit: (formData: Record<string, unknown>) => void
  onChange: (formData: Record<string, unknown>) => void
  isExecuting: boolean
}

export default function ToolForm({ tool, onSubmit, onChange, isExecuting }: ToolFormProps) {
  const schema = useMemo<RJSFSchema>(() => {
    try {
      const parsed = JSON.parse(tool.inputSchema)
      return parsed as RJSFSchema
    }
    catch {
      return { type: 'object', properties: {} }
    }
  }, [tool.inputSchema])

  const hasProperties = schema.properties && Object.keys(schema.properties).length > 0

  const handleSubmit = (data: IChangeEvent) => {
    onSubmit(data.formData ?? {})
  }

  const handleChange = (data: IChangeEvent) => {
    onChange(data.formData ?? {})
  }

  return (
    <div className="rjsf">
      {hasProperties ? (
        <Form
          schema={schema}
          validator={validator}
          onSubmit={handleSubmit}
          onChange={handleChange}
          liveValidate={false}
          showErrorList={false}
          noHtml5Validate
        >
          <button
            type="submit"
            disabled={isExecuting}
            className="mt-4 px-6 py-2.5 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors flex items-center gap-2"
          >
            {isExecuting ? (
              <>
                <span className="animate-spin h-4 w-4 border-2 border-white border-t-transparent rounded-full" />
                Executing...
              </>
            ) : (
              <>
                <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                Execute
              </>
            )}
          </button>
        </Form>
      ) : (
        <div>
          <p className="text-sm text-gray-500 dark:text-gray-400 mb-4">This tool takes no parameters.</p>
          <button
            type="button"
            onClick={() => onSubmit({})}
            disabled={isExecuting}
            className="px-6 py-2.5 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors flex items-center gap-2"
          >
            {isExecuting ? (
              <>
                <span className="animate-spin h-4 w-4 border-2 border-white border-t-transparent rounded-full" />
                Executing...
              </>
            ) : (
              <>
                <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                Execute
              </>
            )}
          </button>
        </div>
      )}
    </div>
  )
}
