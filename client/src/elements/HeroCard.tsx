// elements/HeroCard/HeroCard.tsx
import NoHero from "@/assets/icons/no-hero.svg";

import { cn } from "@/lib/utils";
import type { Hero } from "@/types";
import { rankColorMap, typeIconMap } from "@/utils/typeIconMap";
import {
  ChevronDown,
  ChevronsRight,
  Flame,
  Heart,
  Shield,
  Sword,
} from "lucide-react";
import { useState } from "react";

interface HeroCardProps {
  hero: Hero;
}

export function HeroCard({ hero }: HeroCardProps) {
  const [expanded, setExpanded] = useState(false);
  const rankColor = rankColorMap[hero.rank] ?? "text-white";

  return (
    <div
      className={cn(
        "rounded-xl border border-white/10 bg-white/5 overflow-hidden",
        "transition-all duration-200 hover:border-white/20 hover:bg-white/8",
      )}
    >
      {/* Top accent bar — rank color */}
      <div
        className={cn(
          "h-0.5 w-full",
          hero.rank === "UR" && "bg-yellow-400",
          hero.rank === "SSR" && "bg-purple-400",
          hero.rank === "SR" && "bg-cyan-400",
        )}
      />

      <div className="p-4">
        {/* Hero identity row */}
        <div className="flex gap-4">
          {/* Image */}
          <div className="relative shrink-0">
            <img
              src={hero.imageUrl ?? NoHero}
              alt={hero.name}
              className="w-16 h-20 object-cover rounded-lg"
            />
            {/* Type icon overlaid bottom-right of image */}
            {typeIconMap[hero.type] && (
              <img
                src={typeIconMap[hero.type]}
                alt={hero.type}
                className="absolute -bottom-1 -right-1 w-5 h-5"
              />
            )}
          </div>

          {/* Name + rank + tier */}
          <div className="flex flex-col justify-center gap-1 min-w-0">
            <span className="font-bold text-base text-white leading-tight truncate">
              {hero.name}
            </span>
            <div className="flex items-center gap-2">
              <span className={cn("text-xs font-semibold", rankColor)}>
                {hero.rank}
              </span>
              <span className="text-xs text-white/30">·</span>
              <span className="text-xs text-white/40">{hero.tier}</span>
            </div>
          </div>
        </div>

        {/* Stats row */}
        <div className="mt-4 grid grid-cols-5 gap-1 text-center">
          {[
            { icon: Heart, label: "HP", value: hero.hp, color: "text-red-400" },
            {
              icon: Sword,
              label: "ATK",
              value: hero.atk,
              color: "text-orange-400",
            },
            {
              icon: Shield,
              label: "DEF",
              value: hero.def,
              color: "text-blue-400",
            },
            {
              icon: ChevronsRight,
              label: "SPD",
              value: hero.spd,
              color: "text-green-400",
            },
            {
              icon: Flame,
              label: "PWR",
              value: hero.power,
              color: "text-yellow-400",
            },
          ].map(({ icon: Icon, label, value, color }) => (
            <div key={label} className="flex flex-col items-center gap-0.5">
              <Icon className={cn("w-3.5 h-3.5", color)} />
              <span className="text-[10px] text-white/30 uppercase tracking-wide">
                {label}
              </span>
              <span className="text-xs font-semibold text-white/80">
                {value}
              </span>
            </div>
          ))}
        </div>

        {/* Expand toggle */}
        {hero.skills.length > 0 && (
          <button
            onClick={() => setExpanded((v) => !v)}
            className="mt-3 w-full flex items-center justify-center gap-1 text-xs text-white/30 hover:text-white/60 transition-colors"
          >
            <span>{expanded ? "Hide" : "Skills"}</span>
            <ChevronDown
              className={cn(
                "w-3 h-3 transition-transform duration-200",
                expanded && "rotate-180",
              )}
            />
          </button>
        )}
      </div>

      {/* Skills panel */}
      {expanded && (
        <div className="border-t border-white/10 px-4 py-3 space-y-3">
          {hero.skills.map((skill) => (
            <div key={skill.id}>
              <div className="flex items-center justify-between mb-1">
                <span className="text-xs font-semibold text-white/80">
                  {skill.name}
                </span>
                <span className="text-[10px] text-white/30">
                  CD: {skill.cooldown}s
                </span>
              </div>
              <p className="text-[11px] text-white/50 leading-relaxed">
                {skill.description}
              </p>
              {skill.effects.map((effect) => (
                <div
                  key={effect.id}
                  className="mt-1 pl-2 border-l border-white/10"
                >
                  <span className="text-[10px] font-medium text-white/40">
                    {effect.name}
                  </span>
                  <p className="text-[10px] text-white/30">
                    {effect.description}
                  </p>
                </div>
              ))}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
