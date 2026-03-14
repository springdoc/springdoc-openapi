import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  base: './',
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  build: {
    outDir: '../resources/mcp-ui',
    emptyOutDir: true,
  },
  server: {
    proxy: {
      '/api/mcp-admin': 'http://localhost:8080',
    },
  },
})
