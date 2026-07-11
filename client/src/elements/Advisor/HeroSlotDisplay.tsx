import NoHero from "@/assets/icons/no-hero.svg";
import type { HeroSnapshot, SquadSlot } from "@/types";

type HeroSlotDisplayProps = {
  squad?: SquadSlot | null;
  heroState: HeroSnapshot | null;
};

const rankColor: Record<string, string> = {
  UR: "bg-yellow-400",
  SSR: "bg-purple-400",
  SR: "bg-cyan-400",
};

export function HeroSlotDisplay({ squad, heroState }: HeroSlotDisplayProps) {
  const hero = squad?.hero ?? null;

  const heroBaseHealth = heroState?.maxHp ?? 1;
  const heroCurrentHealth = heroState?.currentHp ?? 1;
  const healthPercentage =
    heroBaseHealth > 0 ? (heroCurrentHealth / heroBaseHealth) * 100 : 0;

  console.log("HeroSlotDisplay - heroState of ", hero?.name, ":", heroState);

  const HeroHealthBar = ({
    healthPercentage,
  }: {
    healthPercentage: number;
  }) => {
    const barColor =
      healthPercentage > 50
        ? "bg-green-500"
        : healthPercentage > 20
          ? "bg-yellow-500"
          : "bg-red-500";

    return (
      <div className="w-50 h-1 bg-gray-700 rounded overflow-hidden">
        <div
          className={`${barColor} w-full h-full rounded`}
          style={{ width: `${healthPercentage}%` }}
        >
          {`${heroCurrentHealth} / ${heroBaseHealth}`}
        </div>
      </div>
    );
  };
  return (
    <div className="relative w-full h-auto justify-around flex flex-col gap-1 items-center">
      <div className="relative w-16 h-20 rounded-xl border border-white/10 bg-white/5 overflow-hidden">
        {hero ? (
          <>
            <div
              className={`absolute top-0 left-0 right-0 h-0.5 rounded-t-xl ${rankColor[hero.rank] ?? "bg-white/20"}`}
            />
            <img
              src={hero.imageUrl ?? NoHero}
              alt={hero.name}
              className="w-full h-full object-cover opacity-100"
              style={{ opacity: heroState?.alive === false ? 0.2 : 1 }}
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
      <HeroHealthBar healthPercentage={healthPercentage} />
      <div className="relative w-50 h-10 rounded-sm border border-white/10 bg-white/5 flex flex-row gap-1">
        {hero?.skills
          ?.sort((a, b) => a.priority - b.priority)
          .map((skill, index) => {
            const skillState = heroState?.skills?.[skill.name];
            const fired = skillState?.firedThisTick ?? false;
            const cooldown = skillState?.cooldownRemaining ?? -1;
            const isPassive = cooldown === -1;
            const opacity = isPassive ? 0.3 : 1;

            return (
              <div
                key={index}
                className={`relative w-full h-full flex items-center justify-center p-1 ${
                  fired ? "ring-1 ring-yellow-400/80" : ""
                }`}
                style={{
                  opacity,
                  boxShadow: fired
                    ? "0 0 8px 2px rgba(250, 204, 21, 0.6)"
                    : "none",
                }}
              >
                <img
                  src={skill.imageUrl ?? NoHero}
                  alt={skill.name}
                  className="w-full h-full object-cover"
                />
                {/* Dark overlay that lifts on fire */}
                {!isPassive && !fired && (
                  <div className="absolute inset-0 bg-black/50 rounded-sm" />
                )}
              </div>
            );
          })}
      </div>
    </div>
  );
}
