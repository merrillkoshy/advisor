type Tier = "cooked" | "coin_flip" | "solid";

type AnalysisResultProps = {
  tier: Tier;
  explanation: string;
};

const tierConfig: Record<Tier, { label: string; color: string; bg: string }> = {
  cooked: {
    label: "You're Cooked",
    color: "text-red-400",
    bg: "border-red-400/20 bg-red-400/5",
  },
  coin_flip: {
    label: "Coin Flip",
    color: "text-yellow-400",
    bg: "border-yellow-400/20 bg-yellow-400/5",
  },
  solid: {
    label: "Solid",
    color: "text-green-400",
    bg: "border-green-400/20 bg-green-400/5",
  },
};

export function AnalysisResult({ tier, explanation }: AnalysisResultProps) {
  const config = tierConfig[tier];
  return (
    <div className={`rounded-xl border p-4 flex flex-col gap-2 ${config.bg}`}>
      <div className={`text-sm font-medium ${config.color}`}>
        {config.label}
      </div>
      <p className="text-sm text-white/70 leading-relaxed">{explanation}</p>
    </div>
  );
}
