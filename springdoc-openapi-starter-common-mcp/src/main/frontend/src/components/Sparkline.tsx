interface SparklineProps {
  data: number[]
  width?: number
  height?: number
  color?: string
  fill?: string
}

export default function Sparkline({ data, width = 80, height = 28, color = '#6366f1', fill }: SparklineProps) {
  if (data.length < 2) {
    return <svg width={width} height={height} />
  }

  const reversed = [...data].reverse()
  const min = Math.min(...reversed)
  const max = Math.max(...reversed)
  const range = max - min || 1
  const pad = 2

  const toX = (i: number) => pad + (i / (reversed.length - 1)) * (width - pad * 2)
  const toY = (v: number) => height - pad - ((v - min) / range) * (height - pad * 2)

  const points = reversed.map((v, i) => `${toX(i)},${toY(v)}`).join(' ')
  const areaPoints = [
    `${toX(0)},${height - pad}`,
    ...reversed.map((v, i) => `${toX(i)},${toY(v)}`),
    `${toX(reversed.length - 1)},${height - pad}`,
  ].join(' ')

  return (
    <svg width={width} height={height} viewBox={`0 0 ${width} ${height}`}>
      {fill && <polygon points={areaPoints} fill={fill} opacity={0.2} />}
      <polyline points={points} fill="none" stroke={color} strokeWidth={1.5} strokeLinecap="round" strokeLinejoin="round" />
      <circle cx={toX(reversed.length - 1)} cy={toY(reversed[reversed.length - 1])} r={2.5} fill={color} />
    </svg>
  )
}
