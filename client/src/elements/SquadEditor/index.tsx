// elements/SquadEditor/SquadEditor.tsx
import { HeroPicker } from "@/elements/HeroPicker";

import type { FORMATION_POSITIONS, Hero, Slot } from "@/types";
import { useState } from "react";

import { Button } from "@/components/ui/button";
import { FORMATION_POSITIONS as POSITIONS } from "@/constants";
import { HeroDetail } from "@/elements/HeroDetail";
import { HeroSlot } from "@/elements/SquadEditor/HeroSlotProps";
import { INITIAL_SLOTS } from "@/elements/SquadEditor/initialValues";
import { Divider } from "@/elements/UI/Divider";
import { useHeroes } from "@/hooks/useHeroes";

type SlotPosition = FORMATION_POSITIONS;

interface SquadEditorProps {
  initialSlots?: Slot[];
  onSave: (slots: Slot[]) => void;
}

const mergeSlots = (initialSlots: Slot[]) => {
  const base = INITIAL_SLOTS.map((s) => ({ ...s }));
  for (const saved of initialSlots) {
    const idx = base.findIndex(
      (s) => s.position === saved.position && s.index === saved.index,
    );
    if (idx !== -1) base[idx] = saved;
  }
  return base;
};

export function SquadEditor({
  initialSlots = INITIAL_SLOTS,
  onSave,
}: SquadEditorProps) {
  const [slots, setSlots] = useState<Slot[]>(mergeSlots(initialSlots));

  const [selectedSlotKey, setSelectedSlotKey] = useState<string | null>(null);
  const [activeSlot, setActiveSlot] = useState<{
    position: SlotPosition;
    index: number;
  } | null>(null);
  const { data: heroes = [] } = useHeroes();

  const selectedSlot = slots.find(
    (s) => selectedSlotKey === `${s.position}-${s.index}`,
  );

  function handleSlotClick(
    position: SlotPosition,
    index: number,
    mode: "edit" | "view",
  ) {
    const key = `${position}-${index}`;
    const heroFound = slots.find(
      (s) => s.position === position && s.index === index,
    )?.hero;

    if (mode === "view")
      if (heroFound) {
        setSelectedSlotKey((prev) => (prev === key ? null : key));
      } else {
        setActiveSlot({ position, index });
      }
    if (mode === "edit") setActiveSlot({ position, index });
  }

  function handleGearStarsChange(
    position: SlotPosition,
    index: number,
    type: keyof Slot["gear"],
    stars: number,
  ) {
    setSlots((prev) =>
      prev.map((slot) =>
        slot.position === position && slot.index === index
          ? { ...slot, gear: { ...slot.gear, [type]: stars } }
          : slot,
      ),
    );
  }

  const pickedHeroIds = new Set(slots.map((s) => s.hero?.id).filter(Boolean));

  function handleHeroPick(hero: Hero) {
    if (!activeSlot) return;
    setSlots((prev) => {
      return prev.map((slot) => {
        return slot.position === activeSlot.position &&
          slot.index === activeSlot.index
          ? { ...slot, hero }
          : slot;
      });
    });
    setActiveSlot(null);
  }

  function handleDrawerClose() {
    setActiveSlot(null);
  }

  const frontSlots = slots.filter((s) => s.position === POSITIONS.FRONT);
  const backSlots = slots.filter((s) => s.position === POSITIONS.BACK);

  const frontDisplay = Array.from(
    { length: 2 },
    (_, i) => frontSlots.find((s) => s.index === i) ?? null,
  );
  const backDisplay = Array.from(
    { length: 3 },
    (_, i) => backSlots.find((s) => s.index === i) ?? null,
  );

  return (
    <>
      <div className="flex flex-col items-center gap-6">
        {/* Front row */}
        <div className="flex gap-4">
          {frontDisplay.map((slot, n) => (
            <HeroSlot
              key={`front-${slot?.index}`}
              slot={slot}
              onClick={() =>
                handleSlotClick(POSITIONS.FRONT, slot?.index ?? n, "view")
              }
              onEdit={() =>
                handleSlotClick(POSITIONS.FRONT, slot?.index ?? n, "edit")
              }
            />
          ))}
        </div>

        {/* Divider */}
        <div className="flex w-full max-w-sm items-center gap-3">
          <Divider />
          <span className="text-[10px] uppercase tracking-widest text-white/20">
            back
          </span>
          <Divider />
        </div>

        {/* Back row */}
        <div className="flex gap-4">
          {backDisplay.map((slot, n) => (
            <HeroSlot
              key={`back-${slot?.index}-${n}`}
              slot={slot}
              onClick={() =>
                handleSlotClick(POSITIONS.BACK, slot?.index ?? n, "view")
              }
              onEdit={() =>
                handleSlotClick(POSITIONS.BACK, slot?.index ?? n, "edit")
              }
            />
          ))}
        </div>
        <Button className={"cursor-pointer"} onClick={() => onSave(slots)}>
          Save
        </Button>

        {/* Right: hero detail panel */}
        {selectedSlot?.hero && (
          <div className="flex-1" id="hero-details">
            <HeroDetail
              hero={selectedSlot.hero}
              gearStars={selectedSlot.gear}
              onStarsChange={(type, stars) =>
                handleGearStarsChange(
                  selectedSlot.position,
                  selectedSlot.index,
                  type,
                  stars,
                )
              }
            />
          </div>
        )}
      </div>

      <HeroPicker
        open={activeSlot !== null}
        heroes={heroes}
        pickedHeroIds={pickedHeroIds as Set<number>}
        onPick={handleHeroPick}
        onClose={handleDrawerClose}
      />
    </>
  );
}
