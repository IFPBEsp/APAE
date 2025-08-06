// components/ui/badge.tsx
import * as React from "react"

interface BadgeProps extends React.HTMLProps<HTMLSpanElement> {
  variant?: "outline" | "filled"   // Permite escolher entre borda ou preenchido
}

const Badge = React.forwardRef<HTMLSpanElement, BadgeProps>(
  ({ variant = "filled", className, children, ...props }, ref) => {
    // Estilos base para o Badge
    const baseStyles = "inline-flex items-center rounded-full px-3 py-1 text-sm font-medium"

    // Estilos diferentes para os dois tipos de Badge
    const outlineStyles = "border-2 border-current text-current" // Com borda
    const filledStyles = "bg-current text-white" // Com fundo preenchido

    // Combinando os estilos conforme a variante escolhida
    return (
      <span
        ref={ref}
        className={`${baseStyles} ${variant === "outline" ? outlineStyles : filledStyles} ${className}`}
        {...props}
      >
        {children}
      </span>
    )
  }
)

Badge.displayName = "Badge"

export { Badge }
