import { Label } from "@/components/ui/label";
import { Slider } from "@/components/ui/slider";
import { DRONE_STAR_TITLES } from "@/constants";
import { getStarTier } from "@/utils/getStarTier";

type DroneHubProps = {
  level: number | readonly number[];
  onLevelChange: (level: number | readonly number[]) => void;
};

export function DroneHub({ level, onLevelChange }: DroneHubProps) {
  const tier = getStarTier(level as number);
  const title = DRONE_STAR_TITLES[tier];

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
