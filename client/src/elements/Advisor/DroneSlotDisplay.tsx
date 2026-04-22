import { DRONE_STAR_TITLES } from "@/constants";
import { SkeletonLoader } from "@/elements/SkeletonLoader";
import { useDrone } from "@/hooks/useDrone";
import { getStarTier } from "@/utils/getStarTier";

export function DroneSlotDisplay({ playerId }: { playerId: number }) {
  const { data, isLoading } = useDrone({
    playerId,
  });
  const tier = getStarTier(data?.drone.level as number);
  const title = DRONE_STAR_TITLES[tier];
  if (isLoading) return <SkeletonLoader items={1} />;
  return (
    <div className="relative w-16 h-20 rounded-xl border border-white/10 bg-white/5 overflow-hidden">
      {data ? (
        <>
          <div className={`absolute top-0 left-0 right-0 h-0.5 rounded-t-xl`} />
          <img
            src={`https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/drone-td-${getStarTier(data.drone.level) + 1}.png`}
            alt={title}
            className="w-full h-full object-cover"
          />
          <div className="absolute bottom-0 left-0 right-0 bg-black/60 px-1 py-0.5">
            <p className="text-[8px] text-white/80 text-center truncate">
              {title}
            </p>
          </div>
        </>
      ) : (
        <div className="w-full h-full flex items-center justify-center">
          <span className="text-white/15 text-xs">DRN</span>
        </div>
      )}
    </div>
  );
}
