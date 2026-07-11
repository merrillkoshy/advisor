import type { Slot, SquadSlot } from "@/types";

export const getSquadByPosition = (
  squad: SquadSlot[] | null,
  position: string,
): SquadSlot | null => {
  return squad?.find((slot) => slot.position === position) ?? null;
};

export const getSquadByPositionForSlotType = (
  squad: Slot[] | null,
  position: string,
): Slot | null => squad?.find((slot) => slot.position === position) ?? null;
