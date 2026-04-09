// elements/HeroPicker/HeroPicker.tsx
import {
  Drawer,
  DrawerContent,
  DrawerHeader,
  DrawerTitle,
} from "@/components/ui/drawer";
import { typeIconMap } from "@/utils/typeIconMap";

import NoHero from "@/assets/icons/no-hero.svg";
import { cn } from "@/lib/utils";
import type { Hero } from "@/types";

const TYPE_SECTIONS: { key: string; label: string }[] = [
  { key: "Tank", label: "Tank" },
  { key: "Aircraft", label: "Air" },
  { key: "MissileVehicle", label: "Missile" },
];

interface HeroPickerProps {
  open: boolean;
  heroes: Hero[];
  pickedHeroIds: Set<number>;
  onPick: (hero: Hero) => void;
  onClose: () => void;
}

export function HeroPicker({
  open,
  heroes,
  pickedHeroIds,
  onPick,
  onClose,
}: HeroPickerProps) {
  return (
    <Drawer open={open} onClose={onClose}>
      <DrawerContent className="max-h-[80vh]">
        <DrawerHeader>
          <DrawerTitle className="text-white">Pick a Hero</DrawerTitle>
        </DrawerHeader>

        <div className="overflow-y-auto px-4 pb-6 space-y-6">
          {TYPE_SECTIONS.map(({ key, label }) => {
            const filtered = heroes.filter((h) => h.type === key);
            if (!filtered.length) return null;

            return (
              <div key={key}>
                {/* Section header */}
                <div className="flex items-center gap-2 mb-3">
                  {typeIconMap[key] && (
                    <img
                      src={typeIconMap[key]}
                      alt={label}
                      className="w-4 h-4"
                    />
                  )}
                  <span className="text-xs uppercase tracking-widest text-white/40">
                    {label}
                  </span>
                  <div className="h-px flex-1 bg-white/10" />
                </div>

                {/* Hero grid */}
                <div className="grid grid-cols-4 gap-2">
                  {filtered.map((hero) => {
                    const isPicked = pickedHeroIds.has(hero.id);
                    return (
                      <button
                        key={hero.id}
                        disabled={isPicked}
                        onClick={() => onPick(hero)}
                        className={cn(
                          "relative flex flex-col items-center gap-1 rounded-lg p-1.5 transition-all duration-150",
                          isPicked
                            ? "opacity-30 cursor-not-allowed"
                            : "hover:bg-white/10 cursor-pointer",
                        )}
                      >
                        {/* Rank accent */}
                        <div
                          className={cn(
                            "absolute top-0 left-0 right-0 h-0.5 rounded-t-lg",
                            hero.rank === "UR" && "bg-yellow-400",
                            hero.rank === "SSR" && "bg-purple-400",
                            hero.rank === "SR" && "bg-cyan-400",
                          )}
                        />
                        <img
                          src={hero.imageUrl ?? NoHero}
                          alt={hero.name}
                          className="w-14 h-16 object-cover rounded-md"
                        />
                        <span className="text-[10px] text-white/60 text-center leading-tight truncate w-full">
                          {hero.name}
                        </span>
                      </button>
                    );
                  })}
                </div>
              </div>
            );
          })}
        </div>
      </DrawerContent>
    </Drawer>
  );
}
