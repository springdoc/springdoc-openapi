import { useState } from 'react'
import type { McpToolInfo, McpToolExecutionResponse } from './types'
import { useTools } from './hooks/useTools'
import { useToolExecution } from './hooks/useToolExecution'
import { useSecuritySettings } from './hooks/useSecuritySettings'
import { useToolMetrics } from './hooks/useToolMetrics'
import Layout, { type ActiveTab } from './components/Layout'
import ToolList from './components/ToolList'
import ToolDetail from './components/ToolDetail'
import ResponseViewer from './components/ResponseViewer'
import SecuritySettings from './components/SecuritySettings'
import KpiDashboard from './components/KpiDashboard'
import AuditDashboard from './components/AuditDashboard'

function App() {
  const [selectedTool, setSelectedTool] = useState<McpToolInfo | null>(null)
  const [response, setResponse] = useState<McpToolExecutionResponse | null>(null)
  const [activeTab, setActiveTab] = useState<ActiveTab>('tools')
  const [lastExecutedArgs, setLastExecutedArgs] = useState<{ toolName: string; args: string } | null>(null)

  const { data: tools, isLoading, error } = useTools()
  const execution = useToolExecution()
  const security = useSecuritySettings()
  const { metrics, recordExecution, clearMetrics } = useToolMetrics()

  const handleExecute = (toolName: string, args: string) => {
    setLastExecutedArgs({ toolName, args })
    execution.mutate(
      { request: { toolName, arguments: args }, authHeaders: security.getAuthHeaders() },
      {
        onSuccess: (data) => {
          setResponse(data)
          recordExecution(toolName, data)
        },
      },
    )
  }

  const handleApprove = () => {
    if (!lastExecutedArgs) return
    const { toolName, args } = lastExecutedArgs
    execution.mutate(
      { request: { toolName, arguments: args, approved: true }, authHeaders: security.getAuthHeaders() },
      {
        onSuccess: (data) => {
          setResponse(data)
          recordExecution(toolName, data)
        },
      },
    )
  }

  return (
    <Layout toolCount={tools?.length ?? 0} activeTab={activeTab} onTabChange={setActiveTab}>
      {activeTab === 'tools' ? (
        <>
          <SecuritySettings
            config={security.config}
            onConfigChange={security.setConfig}
            accessToken={security.accessToken}
            isAuthenticated={security.isAuthenticated}
            tokenError={security.tokenError}
            isRequesting={security.isRequesting}
            onFetchToken={security.fetchToken}
            onClearToken={security.clearToken}
          />
          <div className="flex flex-1 overflow-hidden">
            <ToolList
              tools={tools ?? []}
              isLoading={isLoading}
              error={error}
              selectedTool={selectedTool}
              onSelect={setSelectedTool}
            />
            <div className="flex-1 overflow-y-auto p-6">
              {selectedTool ? (
                <div className="space-y-6">
                  <ToolDetail
                    tool={selectedTool}
                    onExecute={handleExecute}
                    isExecuting={execution.isPending}
                  />
                  {response && (
                    <ResponseViewer
                      response={response}
                      onApprove={response.requiresApproval ? handleApprove : undefined}
                      isApproving={execution.isPending}
                    />
                  )}
                </div>
              ) : (
                <div className="flex items-center justify-center h-full text-gray-400 dark:text-gray-500">
                  <div className="text-center">
                    <img src="./ai-logo-tr.png" alt="" className="mx-auto h-28 w-28 mb-4 opacity-40" />
                    <p className="text-lg font-medium">Select a tool to get started</p>
                    <p className="text-sm mt-1">Choose an MCP tool from the sidebar to view details and execute it</p>
                  </div>
                </div>
              )}
            </div>
          </div>
        </>
      ) : activeTab === 'analytics' ? (
        <KpiDashboard
          metrics={metrics}
          totalTools={tools?.length ?? 0}
          tools={tools ?? []}
          onClear={clearMetrics}
        />
      ) : (
        <AuditDashboard />
      )}
    </Layout>
  )
}

export default App
