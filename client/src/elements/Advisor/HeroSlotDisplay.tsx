import NoHero from "@/assets/icons/no-hero.svg";
import type { SquadSlot } from "@/types";

type HeroSlotDisplayProps = {
  squad?: SquadSlot | null;
};

const rankColor: Record<string, string> = {
  UR: "bg-yellow-400",
  SSR: "bg-purple-400",
  SR: "bg-cyan-400",
};

export function HeroSlotDisplay({ squad }: HeroSlotDisplayProps) {
  const hero = squad?.hero ?? null;
  return (
    <div className="relative w-16 h-20 rounded-xl border border-white/10 bg-white/5 overflow-hidden">
      {hero ? (
        <>
          <div
            className={`absolute top-0 left-0 right-0 h-0.5 rounded-t-xl ${rankColor[hero.rank] ?? "bg-white/20"}`}
          />
          <img
            src={hero.imageUrl ?? NoHero}
            alt={hero.name}
            className="w-full h-full object-cover"
          />
          <div className="absolute bottom-0 left-0 right-0 bg-black/60 px-1 py-0.5">
            <p className="text-[8px] text-white/80 text-center truncate">
              {hero.name}
            </p>
          </div>
        </>
      ) : (
        <div className="w-full h-full flex items-center justify-center">
          <span className="text-white/15 text-xs">—</span>
        </div>
      )}
    </div>
  );
}
