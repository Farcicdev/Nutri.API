import {
    createContext,
    useContext,
    useEffect,
    useState,
    type ReactNode,
} from "react"

import {
    buscarUsuarioAtual,
    entrar,
    renovarSessao,
    sair,
    type LoginData,
    type UsuarioAtual,
} from "@/lib/api"

interface AuthContextData {
    usuario: UsuarioAtual | null
    autenticado: boolean
    carregando: boolean
    login: (dados: LoginData) => Promise<void>
    logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextData | null>(null)

interface AuthProviderProps {
    children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
    const [usuario, setUsuario] = useState<UsuarioAtual | null>(null)
    const [carregando, setCarregando] = useState(true)

    useEffect(() => {
        let ativo = true

        async function restaurarSessao() {
            try {
                await renovarSessao()
                const usuarioAtual = await buscarUsuarioAtual()

                if (ativo) {
                    setUsuario(usuarioAtual)
                }
            } catch {
                if (ativo) {
                    setUsuario(null)
                }
            } finally {
                if (ativo) {
                    setCarregando(false)
                }
            }
        }

        restaurarSessao()

        return () => {
            ativo = false
        }
    }, [])

    async function login(dados: LoginData) {
        await entrar(dados)

        const usuarioAtual = await buscarUsuarioAtual()
        setUsuario(usuarioAtual)
    }

    async function logout() {
        await sair()
        setUsuario(null)
    }

    return (
        <AuthContext.Provider
            value={{
                usuario,
                autenticado: usuario !== null,
                carregando,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const context = useContext(AuthContext)

    if (!context) {
        throw new Error("useAuth deve ser utilizado dentro do AuthProvider")
    }

    return context
}