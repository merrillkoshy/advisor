import { Skeleton } from "@/components/ui/skeleton";

interface SkeletonLoad {
  items?: number;
}
export function SkeletonLoader({ items = 1 }: SkeletonLoad) {
  return (
    <div className="flex items-center gap-4">
      {Array.from({ length: items }).map((_, i) => (
        <div className="flex items-center gap-4" key={`load-skeleton-${i}`}>
          <Skeleton className="h-12 w-12 rounded-full" />
          <div className="space-y-2">
            <Skeleton className="h-4 w-62.5" />
            <Skeleton className="h-4 w-50" />
          </div>
        </div>
      ))}
    </div>
  );
}
