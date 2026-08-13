import axios, { AxiosError, type InternalAxiosRequestConfig } from "axios"

interface TokenResponse {
  accessToken: string
  tokenType: string
  expiresIn: number
}

export interface LoginData {
  email: string
  senha: string
  lembrarDeMim: boolean
}

interface RetryableRequest extends InternalAxiosRequestConfig {
  _retry?: boolean
}

const apiBaseUrl = import.meta.env.VITE_API_URL ?? "/api"

export const api = axios.create({
  baseURL: apiBaseUrl,
  withCredentials: true,
})

const authApi = axios.create({
  baseURL: apiBaseUrl,
  withCredentials: true,
})

let accessToken: string | null = null
let renovacaoEmAndamento: Promise<string> | null = null

function guardarAccessToken(resposta: TokenResponse) {
  accessToken = resposta.accessToken
  return resposta.accessToken
}

export async function entrar(dados: LoginData) {
  accessToken = null
  const { data } = await authApi.post<TokenResponse>("/auth/login", dados)
  guardarAccessToken(data)
}

export async function renovarSessao() {
  if (!renovacaoEmAndamento) {
    renovacaoEmAndamento = authApi
      .post<TokenResponse>("/auth/refresh")
      .then(({ data }) => guardarAccessToken(data))
      .finally(() => {
        renovacaoEmAndamento = null
      })
  }

  return renovacaoEmAndamento
}

export async function sair() {
  try {
    await authApi.post("/auth/logout")
  } finally {
    accessToken = null
  }
}

export function mensagemDaApi(error: unknown) {
  if (axios.isAxiosError<{ message?: string }>(error)) {
    return error.response?.data?.message ?? "Não foi possível conectar ao servidor."
  }
  return "Ocorreu um erro inesperado."
}

export interface UsuarioAtual {
  id: number
  nome: string
  email: string
  role: "ADMIN" | "NUTRICIONISTA"
}

export async function buscarUsuarioAtual() {
  const { data } = await api.get<UsuarioAtual>("/auth/me")
  return data
}

export type StatusConsulta = "AGENDADA" | "REALIZADA" | "CANCELADA"
export type TipoConsulta = "PRIMEIRA_CONSULTA" | "RETORNO" | "ACOMPANHAMENTO"

export interface AtendimentoPorMes {
  ano: number
  mes: number
  quantidade: number
}

export interface ProximaConsulta {
  id: number
  dataConsulta: string
  pacienteId: number
  pacienteNome: string
  tipo: TipoConsulta
  status: StatusConsulta
}

export interface PacienteRecente {
  id: number
  nome: string
  ultimaConsulta: string
  imcAtual: number | null
  percentualGorduraAtual: number | null
}

export interface DashboardResumo {
  totalPacientes: number
  consultasHoje: number
  consultasConfirmadasHoje: number
  consultasProximosSeteDias: number
  avaliacoesRealizadas: number
  avaliacoesRealizadasNoMes: number
  atendimentosPorMes: AtendimentoPorMes[]
  proximasConsultas: ProximaConsulta[]
  pacientesRecentes: PacienteRecente[]
}

export async function buscarResumoDashboard() {
  const { data } = await api.get<DashboardResumo>("/dashboard/resumo")
  return data
}

api.interceptors.request.use((config) => {
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const request = error.config as RetryableRequest | undefined

    if (error.response?.status !== 401 || !request || request._retry) {
      return Promise.reject(error)
    }

    request._retry = true
    try {
      const novoAccessToken = await renovarSessao()
      request.headers.Authorization = `Bearer ${novoAccessToken}`
      return api(request)
    } catch {
      accessToken = null
      return Promise.reject(error)
    }
  },
)
