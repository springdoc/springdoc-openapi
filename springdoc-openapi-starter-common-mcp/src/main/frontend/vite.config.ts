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
    // The Maven build points this at the module output directory (target/classes/mcp-ui) so
    // that the generated dashboard lives under target/, where mvn clean removes it. A
    // standalone build keeps writing next to the sources; those copies are not tracked, and
    // the module excludes them from the jar, which only ever packages what Maven produced.
    outDir: process.env.MCP_UI_OUT_DIR ?? '../resources/mcp-ui',
    emptyOutDir: true,
  },
  server: {
    proxy: {
      '/api/mcp-admin': 'http://localhost:8080',
    },
  },
})
