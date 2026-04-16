import { Label } from "@/components/ui/label";
import { Slider } from "@/components/ui/slider";

const STAR_TITLES = [
  'TD-1 "Pathfinder"',
  'TD-2 "Blaster"',
  'TD-3 "Silver Knight"',
  'TD-4 "Phantom"',
  'TD-5 "Destroyer"',
  'TD-6 "Colossus"',
];

const getStarTier = (level: number) => {
  if (level < 30) return 0;
  if (level < 50) return 1;
  if (level < 70) return 2;
  if (level < 90) return 3;
  if (level < 110) return 4;
  return 5;
};

type DroneHubProps = {
  level: number | readonly number[];
  onLevelChange: (level: number | readonly number[]) => void;
};

export function DroneHub({ level, onLevelChange }: DroneHubProps) {
  const tier = getStarTier(level as number);
  const title = STAR_TITLES[tier];

  return (
    <div className="flex flex-col items-center gap-2 w-1/2 space-y-15">
      <div className="text-lg text-white/35 tracking-widest uppercase">
        {title}
      </div>
      <div className="w-50 h-50 rounded-full border border-white/15 bg-white/4 flex items-center justify-center">
        <img
          src={`https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/drone-td-${tier + 1}.png`}
          alt={title}
          className="w-50 h-50 object-contain rounded-md"
        />
      </div>
      <div className="mx-auto grid w-full max-w-xs gap-3">
        <div className="flex items-center justify-between gap-2">
          <Label htmlFor="drone-level">Level</Label>
          <span className="text-sm text-muted-foreground">{level}</span>
        </div>
        <Slider
          id="drone-level"
          value={level}
          onValueChange={onLevelChange}
          min={0}
          max={250}
          step={10}
        />
      </div>
      <div className="text-md text-white/50">max 250</div>
    </div>
  );
}
