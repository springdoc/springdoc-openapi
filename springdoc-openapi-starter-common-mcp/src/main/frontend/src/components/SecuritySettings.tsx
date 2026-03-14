import { useState } from 'react'
import type { SecurityConfig, AuthMode } from '../hooks/useSecuritySettings'

interface SecuritySettingsProps {
  config: SecurityConfig
  onConfigChange: (config: SecurityConfig) => void
  accessToken: string | null
  isAuthenticated: boolean
  tokenError: string | null
  isRequesting: boolean
  onFetchToken: () => void
  onClearToken: () => void
}

const AUTH_MODES: { value: AuthMode; label: string }[] = [
  { value: 'none', label: 'None' },
  { value: 'oauth2', label: 'OAuth2' },
  { value: 'apikey', label: 'API Key' },
]

export default function SecuritySettings({
  config,
  onConfigChange,
  accessToken,
  isAuthenticated,
  tokenError,
  isRequesting,
  onFetchToken,
  onClearToken,
}: SecuritySettingsProps) {
  const [expanded, setExpanded] = useState(false)
  const [copied, setCopied] = useState(false)

  const handleCopyToken = () => {
    if (accessToken) {
      navigator.clipboard.writeText(accessToken).then(() => {
        setCopied(true)
        setTimeout(() => setCopied(false), 2000)
      })
    }
  }

  const truncatedToken = accessToken
    ? accessToken.length > 40
      ? accessToken.substring(0, 20) + '...' + accessToken.substring(accessToken.length - 20)
      : accessToken
    : null

  return (
    <div className="border-b border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800">
      <button
        onClick={() => setExpanded(!expanded)}
        className="w-full flex items-center justify-between px-6 py-2.5 hover:bg-gray-50 dark:hover:bg-gray-750 transition-colors"
      >
        <div className="flex items-center gap-2">
          <svg className="h-4 w-4 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
          </svg>
          <span className="text-sm font-medium text-gray-700 dark:text-gray-300">Security Settings</span>
          {isAuthenticated && (
            <span className="px-2 py-0.5 text-[10px] font-bold bg-green-100 text-green-800 dark:bg-green-900/50 dark:text-green-300 rounded-full">
              {config.authMode === 'oauth2' ? 'TOKEN ACTIVE' : 'API KEY SET'}
            </span>
          )}
        </div>
        <svg
          className={`h-4 w-4 text-gray-400 transition-transform ${expanded ? 'rotate-180' : ''}`}
          fill="none" viewBox="0 0 24 24" stroke="currentColor"
        >
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </svg>
      </button>

      {expanded && (
        <div className="px-6 pb-4 space-y-3">
          {/* Auth Mode Selector */}
          <div className="flex gap-1 p-0.5 bg-gray-100 dark:bg-gray-900 rounded-lg w-fit">
            {AUTH_MODES.map((mode) => (
              <button
                key={mode.value}
                onClick={() => onConfigChange({ ...config, authMode: mode.value })}
                className={`px-3 py-1 text-xs font-medium rounded-md transition-colors ${
                  config.authMode === mode.value
                    ? 'bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 shadow-sm'
                    : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300'
                }`}
              >
                {mode.label}
              </button>
            ))}
          </div>

          {/* OAuth2 Settings */}
          {config.authMode === 'oauth2' && (
            <>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-600 dark:text-gray-400 mb-1">
                    Token Endpoint
                  </label>
                  <input
                    type="text"
                    value={config.tokenEndpoint}
                    onChange={(e) => onConfigChange({ ...config, tokenEndpoint: e.target.value })}
                    className="w-full px-3 py-1.5 text-sm border border-gray-300 dark:border-gray-600 rounded-md bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="http://localhost:9000/oauth2/token"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-600 dark:text-gray-400 mb-1">
                    Grant Type
                  </label>
                  <select
                    value={config.grantType}
                    onChange={(e) => onConfigChange({ ...config, grantType: e.target.value })}
                    className="w-full px-3 py-1.5 text-sm border border-gray-300 dark:border-gray-600 rounded-md bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="client_credentials">client_credentials</option>
                    <option value="password">password</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-600 dark:text-gray-400 mb-1">
                    Client ID
                  </label>
                  <input
                    type="text"
                    value={config.clientId}
                    onChange={(e) => onConfigChange({ ...config, clientId: e.target.value })}
                    className="w-full px-3 py-1.5 text-sm border border-gray-300 dark:border-gray-600 rounded-md bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="default-client"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-600 dark:text-gray-400 mb-1">
                    Client Secret
                  </label>
                  <input
                    type="password"
                    value={config.clientSecret}
                    onChange={(e) => onConfigChange({ ...config, clientSecret: e.target.value })}
                    className="w-full px-3 py-1.5 text-sm border border-gray-300 dark:border-gray-600 rounded-md bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="default-secret"
                  />
                </div>
              </div>

              <div className="flex items-center gap-2">
                <button
                  onClick={onFetchToken}
                  disabled={isRequesting}
                  className="px-4 py-1.5 text-sm font-medium bg-amber-600 text-white rounded-lg hover:bg-amber-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors flex items-center gap-2"
                >
                  {isRequesting ? (
                    <>
                      <span className="animate-spin h-3.5 w-3.5 border-2 border-white border-t-transparent rounded-full" />
                      Requesting...
                    </>
                  ) : (
                    <>
                      <svg className="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                          d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
                      </svg>
                      Get Token
                    </>
                  )}
                </button>
                {accessToken && (
                  <button
                    onClick={onClearToken}
                    className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 bg-gray-100 dark:bg-gray-700 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors"
                  >
                    Clear
                  </button>
                )}
              </div>

              {tokenError && (
                <div className="p-2.5 text-sm text-red-600 dark:text-red-400 bg-red-50 dark:bg-red-900/20 rounded-md border border-red-200 dark:border-red-800">
                  {tokenError}
                </div>
              )}

              {accessToken && (
                <div className="flex items-center gap-2 p-2.5 bg-green-50 dark:bg-green-900/20 rounded-md border border-green-200 dark:border-green-800">
                  <svg className="h-4 w-4 text-green-600 dark:text-green-400 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                  </svg>
                  <code className="text-xs text-green-800 dark:text-green-300 font-mono flex-1 truncate">
                    {truncatedToken}
                  </code>
                  <button
                    onClick={handleCopyToken}
                    className="flex-shrink-0 px-2 py-1 text-[10px] font-medium text-green-700 dark:text-green-300 bg-green-100 dark:bg-green-800/50 rounded hover:bg-green-200 dark:hover:bg-green-700/50 transition-colors"
                  >
                    {copied ? 'Copied!' : 'Copy'}
                  </button>
                </div>
              )}
            </>
          )}

          {/* API Key Settings */}
          {config.authMode === 'apikey' && (
            <>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-600 dark:text-gray-400 mb-1">
                    Header Name
                  </label>
                  <input
                    type="text"
                    value={config.apiKeyHeaderName}
                    onChange={(e) => onConfigChange({ ...config, apiKeyHeaderName: e.target.value })}
                    className="w-full px-3 py-1.5 text-sm border border-gray-300 dark:border-gray-600 rounded-md bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="X-API-Key"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-600 dark:text-gray-400 mb-1">
                    API Key
                  </label>
                  <input
                    type="password"
                    value={config.apiKeyValue}
                    onChange={(e) => onConfigChange({ ...config, apiKeyValue: e.target.value })}
                    className="w-full px-3 py-1.5 text-sm border border-gray-300 dark:border-gray-600 rounded-md bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="your-api-key"
                  />
                </div>
              </div>

              {config.apiKeyValue && (
                <div className="flex items-center gap-2 p-2.5 bg-green-50 dark:bg-green-900/20 rounded-md border border-green-200 dark:border-green-800">
                  <svg className="h-4 w-4 text-green-600 dark:text-green-400 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                  </svg>
                  <span className="text-xs text-green-800 dark:text-green-300">
                    API Key configured — will be sent as <code className="font-mono bg-green-100 dark:bg-green-800/50 px-1 rounded">{config.apiKeyHeaderName || 'X-API-Key'}</code> header
                  </span>
                </div>
              )}
            </>
          )}

          {/* None mode info */}
          {config.authMode === 'none' && (
            <p className="text-xs text-gray-500 dark:text-gray-400">
              No authentication configured. Tool execution requests will be sent without credentials.
            </p>
          )}
        </div>
      )}
    </div>
  )
}
