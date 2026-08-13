import { createContext, useContext, useEffect, useState, type ReactNode } from "react"

type Theme = "light" | "dark"

interface ThemeContextData {
  theme: Theme
  alternarTema: () => void
}

const ThemeContext = createContext<ThemeContextData | null>(null)

function temaInicial(): Theme {
  const temaSalvo = localStorage.getItem("nutriapp-theme")
  if (temaSalvo === "light" || temaSalvo === "dark") return temaSalvo

  return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light"
}

export function ThemeProvider({ children }: { children: ReactNode }) {
  const [theme, setTheme] = useState<Theme>(temaInicial)

  useEffect(() => {
    const raiz = document.documentElement
    raiz.classList.remove("light", "dark")
    raiz.classList.add(theme)
    localStorage.setItem("nutriapp-theme", theme)
  }, [theme])

  function alternarTema() {
    setTheme((atual) => (atual === "light" ? "dark" : "light"))
  }

  return (
    <ThemeContext.Provider value={{ theme, alternarTema }}>
      {children}
    </ThemeContext.Provider>
  )
}

export function useTheme() {
  const context = useContext(ThemeContext)
  if (!context) throw new Error("useTheme deve ser utilizado dentro do ThemeProvider")
  return context
}
