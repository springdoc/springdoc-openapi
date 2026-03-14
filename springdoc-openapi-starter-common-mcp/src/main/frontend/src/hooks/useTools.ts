import { useQuery } from '@tanstack/react-query'
import { fetchTools } from '../api/mcpAdminApi'

export function useTools() {
  return useQuery({
    queryKey: ['mcp-tools'],
    queryFn: fetchTools,
  })
}
