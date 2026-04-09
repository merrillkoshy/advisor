import { heroesQuery } from "@/api/heroes";

import { HeroCard } from "@/elements/HeroCard";
import { SkeletonLoader } from "@/elements/SkeletonLoader";
import { useHeroes } from "@/hooks/useHeroes";

import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/heroes")({
  loader: async ({ context }) => {
    await context.queryClient.ensureQueryData(heroesQuery);
  },
  component: HeroesPage,
});
function HeroesPage() {
  const { data, isLoading, error } = useHeroes();

  if (isLoading) return <SkeletonLoader items={5} />;
  if (error) return <div>Something went wrong</div>;

  return (
    <div className="grid grid-cols-2 gap-4">
      {data?.map((hero) => (
        <HeroCard key={hero.id} hero={hero} />
      ))}
    </div>
  );
}
