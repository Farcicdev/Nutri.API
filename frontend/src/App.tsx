import { Navigate, Route, Routes } from "react-router-dom"

import Dashboard from "@/pages/Dashboard/Dashboard"
import Login from "@/pages/Login/Login"
import { PublicOnlyRoute } from "./auth/public-only-route"
import { ProtectedRoute } from "./auth/protected-route"

function App() {
  return (
      <Routes>
          <Route element={<PublicOnlyRoute />}>
              <Route path="/login" element={<Login />} />
          </Route>

          <Route element={<ProtectedRoute />}>
              <Route path="/dashboard" element={<Dashboard />} />
          </Route>

          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
  )
}

export default App
