import axios, { AxiosError, type InternalAxiosRequestConfig } from "axios";

interface TokenResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
}

export interface LoginData {
  email: string;
  senha: string;
  lembrarDeMim: boolean;
}

interface RetryableRequest extends InternalAxiosRequestConfig {
  _retry?: boolean;
}

const apiBaseUrl = import.meta.env.VITE_API_URL ?? "/api";

export const api = axios.create({
  baseURL: apiBaseUrl,
  withCredentials: true,
});

const authApi = axios.create({
  baseURL: apiBaseUrl,
  withCredentials: true,
});

let accessToken: string | null = null;
let renovacaoEmAndamento: Promise<string> | null = null;

function guardarAccessToken(resposta: TokenResponse) {
  accessToken = resposta.accessToken;
  return resposta.accessToken;
}

export async function entrar(dados: LoginData) {
  accessToken = null;
  const { data } = await authApi.post<TokenResponse>("/auth/login", dados);
  guardarAccessToken(data);
}

export async function renovarSessao() {
  if (!renovacaoEmAndamento) {
    renovacaoEmAndamento = authApi
      .post<TokenResponse>("/auth/refresh")
      .then(({ data }) => guardarAccessToken(data))
      .finally(() => {
        renovacaoEmAndamento = null;
      });
  }

  return renovacaoEmAndamento;
}

export async function sair() {
  try {
    await authApi.post("/auth/logout");
  } finally {
    accessToken = null;
  }
}

export function mensagemDaApi(error: unknown) {
  if (axios.isAxiosError<{ message?: string }>(error)) {
    return (
      error.response?.data?.message ?? "Não foi possível conectar ao servidor."
    );
  }
  return "Ocorreu um erro inesperado.";
}

export interface UsuarioAtual {
  id: number;
  nome: string;
  email: string;
  role: "ADMIN" | "NUTRICIONISTA";
}

export async function buscarUsuarioAtual() {
  const { data } = await api.get<UsuarioAtual>("/auth/me");
  return data;
}

export type StatusConsulta = "AGENDADA" | "REALIZADA" | "CANCELADA";
export type TipoConsulta = "PRIMEIRA_CONSULTA" | "RETORNO" | "ACOMPANHAMENTO";

export interface AtendimentoPorMes {
  ano: number;
  mes: number;
  quantidade: number;
}

export interface ProximaConsulta {
  id: number;
  dataConsulta: string;
  pacienteId: number;
  pacienteNome: string;
  tipo: TipoConsulta;
  status: StatusConsulta;
}

export interface PacienteRecente {
  id: number;
  nome: string;
  ultimaConsulta: string;
  imcAtual: number | null;
  percentualGorduraAtual: number | null;
}

export interface DashboardResumo {
  totalPacientes: number;
  consultasHoje: number;
  consultasConfirmadasHoje: number;
  consultasProximosSeteDias: number;
  avaliacoesRealizadas: number;
  avaliacoesRealizadasNoMes: number;
  atendimentosPorMes: AtendimentoPorMes[];
  proximasConsultas: ProximaConsulta[];
  pacientesRecentes: PacienteRecente[];
}

export async function buscarResumoDashboard() {
  const { data } = await api.get<DashboardResumo>("/dashboard/resumo");
  return data;
}

export type Sexo = "MASCULINO" | "FEMININO";

export interface Paciente {
  id: number;
  nome: string;
  dataNascimento: string;
  sexo: Sexo;
  email: string;
  telefone: string | null;
  observacoes: string | null;
  ativo: boolean;
}

export interface PacientePayload {
  nome: string;
  dataNascimento: string;
  sexo: Sexo;
  email: string;
  telefone?: string;
  observacoes?: string;
}

export const listarPacientes = async () =>
  (await api.get<Paciente[]>("/pacientes")).data;
export const criarPaciente = async (payload: PacientePayload) =>
  (await api.post<Paciente>("/pacientes", payload)).data;
export const atualizarPaciente = async (id: number, payload: PacientePayload) =>
  (await api.put<Paciente>(`/pacientes/${id}`, payload)).data;
export const atualizarStatusPaciente = async (id: number, ativo: boolean) =>
  (await api.patch<Paciente>(`/pacientes/${id}/ativo`, { ativo })).data;
export const removerPaciente = async (id: number) =>
  api.delete(`/pacientes/${id}`);

export interface Consulta extends ProximaConsulta {
  observacoes: string | null;
  criadoEm: string;
  atualizadoEm: string;
  nutricionistaId: number;
  nutricionistaNome: string;
}

export interface ConsultaPayload {
  dataConsulta: string;
  tipo: TipoConsulta;
  observacoes?: string;
  pacienteId: number;
}

export const listarConsultas = async () =>
  (await api.get<Consulta[]>("/consultas")).data;
export const criarConsulta = async (payload: ConsultaPayload) =>
  (await api.post<Consulta>("/consultas", payload)).data;
export const atualizarConsulta = async (id: number, payload: ConsultaPayload) =>
  (await api.put<Consulta>(`/consultas/${id}`, payload)).data;
export const atualizarStatusConsulta = async (
  id: number,
  status: StatusConsulta,
) => (await api.patch<Consulta>(`/consultas/${id}/status`, { status })).data;
export const removerConsulta = async (id: number) =>
  api.delete(`/consultas/${id}`);

export type ProtocoloDobras =
  | "JACKSON_POLLACK_3_PONTOS"
  | "JACKSON_POLLACK_7_PONTOS"
  | "FAULKNER";

export interface Avaliacao {
  id: number;
  consultaId: number;
  pacienteId: number;
  dataAvaliacao: string;
  peso: number;
  altura: number;
  cintura: number | null;
  quadril: number | null;
  protocoloDobras: ProtocoloDobras;
  imc: number;
  percentualGordura: number;
  massaGorda: number;
  massaMagra: number;
  observacoes: string | null;
}

export interface AvaliacaoPayload {
  consultaId: number;
  peso: number;
  altura: number;
  dataAvaliacao: string;
  cintura?: number;
  quadril?: number;
  peitoral?: number;
  tricipital?: number;
  subescapular?: number;
  bicipital?: number;
  suprailiaca?: number;
  abdominal?: number;
  coxa?: number;
  axilarMedia?: number;
  protocoloDobras: ProtocoloDobras;
  observacoes?: string;
}

export const listarAvaliacoesPorConsulta = async (consultaId: number) =>
  (
    await api.get<Avaliacao[]>(
      `/avaliacoes-antropometricas/consulta/${consultaId}`,
    )
  ).data;
export const criarAvaliacao = async (payload: AvaliacaoPayload) =>
  (await api.post<Avaliacao>("/avaliacoes-antropometricas", payload)).data;

api.interceptors.request.use((config) => {
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const request = error.config as RetryableRequest | undefined;

    if (error.response?.status !== 401 || !request || request._retry) {
      return Promise.reject(error);
    }

    request._retry = true;
    try {
      const novoAccessToken = await renovarSessao();
      request.headers.Authorization = `Bearer ${novoAccessToken}`;
      return api(request);
    } catch {
      accessToken = null;
      return Promise.reject(error);
    }
  },
);
