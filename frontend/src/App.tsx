import { Navigate, Route, Routes } from "react-router-dom"

import Dashboard from "@/pages/Dashboard/Dashboard"
import Pacientes from "@/pages/Pacientes/Pacientes"
import Consultas from "@/pages/Consultas/Consultas"
import Avaliacoes from "@/pages/Avaliacoes/Avaliacoes"
import Configuracoes from "@/pages/Configuracoes/Configuracoes"
import Login from "@/pages/Login/Login"
import { AppLayout } from "@/components/layout/app-layout"
import { PublicOnlyRoute } from "./auth/public-only-route"
import { ProtectedRoute } from "./auth/protected-route"

function App() {
  return (
      <Routes>
          <Route element={<PublicOnlyRoute />}>
              <Route path="/login" element={<Login />} />
          </Route>

          <Route element={<ProtectedRoute />}>
              <Route element={<AppLayout />}>
                  <Route path="/dashboard" element={<Dashboard />} />
                  <Route path="/pacientes" element={<Pacientes />} />
                  <Route path="/consultas" element={<Consultas />} />
                  <Route path="/avaliacoes" element={<Avaliacoes />} />
                  <Route path="/configuracoes" element={<Configuracoes />} />
              </Route>
          </Route>

          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
  )
}

export default App
