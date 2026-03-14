import { useState, useCallback } from 'react'

export type AuthMode = 'none' | 'oauth2' | 'apikey'

export interface SecurityConfig {
  authMode: AuthMode
  // OAuth2
  tokenEndpoint: string
  clientId: string
  clientSecret: string
  grantType: string
  // API Key
  apiKeyHeaderName: string
  apiKeyValue: string
}

const DEFAULT_CONFIG: SecurityConfig = {
  authMode: 'none',
  tokenEndpoint: 'http://localhost:9000/oauth2/token',
  clientId: 'default-client',
  clientSecret: 'default-secret',
  grantType: 'client_credentials',
  apiKeyHeaderName: 'X-API-Key',
  apiKeyValue: '',
}

export function useSecuritySettings() {
  const [config, setConfig] = useState<SecurityConfig>(DEFAULT_CONFIG)
  const [accessToken, setAccessToken] = useState<string | null>(null)
  const [tokenError, setTokenError] = useState<string | null>(null)
  const [isRequesting, setIsRequesting] = useState(false)

  const fetchToken = useCallback(async () => {
    setIsRequesting(true)
    setTokenError(null)
    try {
      const body = new URLSearchParams({ grant_type: config.grantType })
      const headers: HeadersInit = { 'Content-Type': 'application/x-www-form-urlencoded' }

      if (config.clientSecret) {
        headers['Authorization'] = 'Basic ' + btoa(`${config.clientId}:${config.clientSecret}`)
      } else {
        body.append('client_id', config.clientId)
      }

      const res = await fetch(config.tokenEndpoint, {
        method: 'POST',
        headers,
        body,
      })

      if (!res.ok) {
        const text = await res.text()
        throw new Error(`${res.status} ${res.statusText}: ${text}`)
      }

      const data = await res.json()
      setAccessToken(data.access_token)
    } catch (err) {
      setTokenError(err instanceof Error ? err.message : String(err))
      setAccessToken(null)
    } finally {
      setIsRequesting(false)
    }
  }, [config])

  const clearToken = useCallback(() => {
    setAccessToken(null)
    setTokenError(null)
  }, [])

  const getAuthHeaders = useCallback((): Record<string, string> => {
    if (config.authMode === 'oauth2' && accessToken) {
      return { 'Authorization': `Bearer ${accessToken}` }
    }
    if (config.authMode === 'apikey' && config.apiKeyValue) {
      return { [config.apiKeyHeaderName || 'X-API-Key']: config.apiKeyValue }
    }
    return {}
  }, [config.authMode, config.apiKeyHeaderName, config.apiKeyValue, accessToken])

  const isAuthenticated =
    config.authMode === 'oauth2' ? !!accessToken :
    config.authMode === 'apikey' ? !!config.apiKeyValue : false

  return { config, setConfig, accessToken, tokenError, isRequesting, fetchToken, clearToken, getAuthHeaders, isAuthenticated }
}
