// elements/HeroDetail/HeroDetail.tsx
import NoHero from "@/assets/icons/no-hero.svg";

import { useGears } from "@/hooks/useGears";
import { cn } from "@/lib/utils";
import type { Gear, Hero } from "@/types";
import { rankColorMap, typeIconMap } from "@/utils/typeIconMap";

interface GearStars {
  gun: number;
  armor: number;
  chip: number;
  radar: number;
}

interface HeroDetailProps {
  hero: Hero;
  gearStars: GearStars;
  onStarsChange: (type: keyof GearStars, stars: number) => void;
}

const GEAR_POSITIONS = [
  { key: "gun" as const, type: "Gun", label: "Gun", gridArea: "gun" },
  { key: "armor" as const, type: "Armor", label: "Armor", gridArea: "armor" },
  { key: "chip" as const, type: "Data Chip", label: "Chip", gridArea: "chip" },
  { key: "radar" as const, type: "Radar", label: "Radar", gridArea: "radar" },
];

function computeStats(gear: Gear, stars: number) {
  const level = 40 + stars * 10;
  return gear.stats.map((stat) => ({
    key: stat.statKey.key,
    description: stat.statKey.description,
    valueType: stat.statKey.valueType,
    value: stat.baseValue + stat.increment * (level / 10),
  }));
}

function formatValue(value: number, valueType: string) {
  if (valueType === "float") return `${value.toFixed(1)}%`;
  return Math.round(value).toLocaleString();
}

interface GearSlotProps {
  gearType: string;
  label: string;
  stars: number;
  gear: Gear | undefined;
  onStarsChange: (stars: number) => void;
}

function GearSlot({
  gearType,
  label,
  stars,
  gear,
  onStarsChange,
}: GearSlotProps) {
  const isMythic = stars === 5;
  const computedStats = gear ? computeStats(gear, stars) : [];
  const displayName = gear
    ? isMythic
      ? gear.mythicName
      : gear.baseName
    : label;

  return (
    <div
      className={cn(
        "rounded-xl border p-3 space-y-2 transition-all duration-200",
        isMythic
          ? "border-red-500/40 bg-red-500/5"
          : "border-white/10 bg-white/5",
      )}
    >
      {/* Gear name */}
      <p
        className={cn(
          "text-[10px] font-semibold leading-tight",
          isMythic ? "text-red-400" : "text-white/60",
        )}
      >
        {displayName}
      </p>

      {/* Star dots */}
      <div className="flex gap-1">
        {Array.from({ length: 5 }).map((_, i) => (
          <button
            key={i}
            onClick={() => onStarsChange(i + 1 === stars ? 0 : i + 1)}
            className={cn(
              "w-3 h-3 rounded-full border transition-all duration-150",
              i < stars
                ? isMythic
                  ? "bg-red-400 border-red-400"
                  : "bg-yellow-400 border-yellow-400"
                : "bg-transparent border-white/20 hover:border-white/40",
            )}
          />
        ))}
      </div>

      {/* Computed stats */}
      <div className="space-y-0.5">
        {computedStats.map((stat) => (
          <div key={stat.key} className="flex justify-between items-center">
            <span className="text-[9px] text-white/30 truncate max-w-[60%]">
              {stat.description}
            </span>
            <span className="text-[9px] font-semibold text-white/60">
              {formatValue(stat.value, stat.valueType)}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}

export function HeroDetail({
  hero,
  gearStars,
  onStarsChange,
}: HeroDetailProps) {
  const { data: gears = [] } = useGears();

  const getGear = (type: string) => gears.find((g) => g.type === type);

  return (
    <div className="rounded-xl border border-white/10 bg-white/5 p-6">
      {/* Hero identity */}
      <div className="flex items-center gap-4 mb-6">
        <div className="relative shrink-0">
          <img
            src={hero.imageUrl ?? NoHero}
            alt={hero.name}
            className="w-16 h-20 object-cover rounded-lg"
          />
          {typeIconMap[hero.type] && (
            <img
              src={typeIconMap[hero.type]}
              alt={hero.type}
              className="absolute -bottom-1 -right-1 w-5 h-5"
            />
          )}
        </div>
        <div>
          <h2 className="text-base font-bold text-white">{hero.name}</h2>
          <span
            className={cn(
              "text-xs font-semibold",
              rankColorMap[hero.rank] ?? "text-white",
            )}
          >
            {hero.rank}
          </span>
          <span className="text-xs text-white/30 ml-2">{hero.tier}</span>
        </div>
      </div>

      {/* Gear grid — gun/armor top, hero center, chip/radar bottom */}
      <div className="grid grid-cols-2 gap-3">
        {/* Top row: Gun + Armor */}
        <GearSlot
          gearType="Gun"
          label="Gun"
          stars={gearStars.gun}
          gear={getGear("Gun")}
          onStarsChange={(s) => onStarsChange("gun", s)}
        />
        <GearSlot
          gearType="Armor"
          label="Armor"
          stars={gearStars.armor}
          gear={getGear("Armor")}
          onStarsChange={(s) => onStarsChange("armor", s)}
        />
        {/* Bottom row: Chip + Radar */}
        <GearSlot
          gearType="Data Chip"
          label="Chip"
          stars={gearStars.chip}
          gear={getGear("Data Chip")}
          onStarsChange={(s) => onStarsChange("chip", s)}
        />
        <GearSlot
          gearType="Radar"
          label="Radar"
          stars={gearStars.radar}
          gear={getGear("Radar")}
          onStarsChange={(s) => onStarsChange("radar", s)}
        />
      </div>
    </div>
  );
}
