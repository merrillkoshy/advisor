import { DroneLevelStepper } from "./DroneLevelStepper";

type DroneComponentSlotProps = {
  name: string;
  imageUrl: string;
  level: number;
  maxLevel: number;
  onLevelChange: (level: number) => void;
  side: "left" | "right";
};

export function DroneComponentSlot({
  name,
  imageUrl,
  level,
  maxLevel,
  onLevelChange,
  side,
}: DroneComponentSlotProps) {
  return (
    <div
      className={`flex flex-col gap-2 p-3 rounded-xl border border-white/10 bg-white/4 w-36 ${side === "right" ? "items-end text-right" : "items-start text-left"}`}
    >
      <img
        src={imageUrl}
        alt={name}
        className="w-full h-full object-cover rounded-xl"
      />
      <div className="text-sm text-white leading-tight">{name}</div>
      <div className="w-full h-0.5 rounded-full bg-white/8 overflow-hidden">
        <div
          className="h-full rounded-full bg-white/30 transition-all duration-200"
          style={{ width: `${Math.round((level / maxLevel) * 100)}%` }}
        />
      </div>
      <DroneLevelStepper
        value={level}
        min={0}
        max={maxLevel}
        onChange={onLevelChange}
      />
    </div>
  );
}
