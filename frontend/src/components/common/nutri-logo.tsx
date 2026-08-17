import { Leaf } from "lucide-react";
import { cn } from "@/lib/utils";

interface NutriLogoProps {
  className?: string;
  showText?: boolean;
}

export function NutriLogo({ className, showText = true }: NutriLogoProps) {
  return (
    <div className={cn("flex items-center gap-2.5", className)}>
      <div className="flex size-9 items-center justify-center rounded-xl bg-primary text-primary-foreground shadow-sm">
        <Leaf className="size-5" aria-hidden="true" />
      </div>
      {showText && (
        <span className="font-serif text-xl font-bold tracking-tight text-foreground">
          Nutri<span className="text-primary">App</span>
        </span>
      )}
    </div>
  );
}
