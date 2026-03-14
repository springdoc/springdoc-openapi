import { cn } from '../lib/utils'

const METHOD_COLORS: Record<string, string> = {
  GET: 'bg-green-100 text-green-800 dark:bg-green-900/50 dark:text-green-300',
  POST: 'bg-blue-100 text-blue-800 dark:bg-blue-900/50 dark:text-blue-300',
  PUT: 'bg-orange-100 text-orange-800 dark:bg-orange-900/50 dark:text-orange-300',
  PATCH: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/50 dark:text-yellow-300',
  DELETE: 'bg-red-100 text-red-800 dark:bg-red-900/50 dark:text-red-300',
}

interface HttpMethodBadgeProps {
  method: string | null
  className?: string
}

export default function HttpMethodBadge({ method, className }: HttpMethodBadgeProps) {
  if (!method) return null
  const colorClass = METHOD_COLORS[method.toUpperCase()] ?? 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300'
  return (
    <span className={cn('px-1.5 py-0.5 text-[10px] font-bold rounded uppercase tracking-wide', colorClass, className)}>
      {method}
    </span>
  )
}
