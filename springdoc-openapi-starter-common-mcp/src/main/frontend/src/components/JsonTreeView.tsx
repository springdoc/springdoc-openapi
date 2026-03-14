import { JsonView, darkStyles, defaultStyles } from 'react-json-view-lite'
import 'react-json-view-lite/dist/index.css'

interface JsonTreeViewProps {
  data: object | Array<unknown>
}

export default function JsonTreeView({ data }: JsonTreeViewProps) {
  const isDark = document.documentElement.classList.contains('dark')
  return (
    <JsonView
      data={data as object}
      shouldExpandNode={() => true}
      style={isDark ? darkStyles : defaultStyles}
    />
  )
}
