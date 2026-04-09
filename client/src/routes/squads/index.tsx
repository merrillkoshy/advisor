import { SquadCard } from "@/elements/SquadCard";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

export const Route = createFileRoute("/squads/")({
  component: SquadsPage,
});

function SquadsPage() {
  const navigate = useNavigate();

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white">Squads</h1>
        <p className="mt-1 text-sm text-white/40">
          Configure your hero formations and gear loadouts.
        </p>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        {([1, 2, 3] as const).map((n) => (
          <SquadCard
            key={n}
            squadNumber={n}
            onClick={() =>
              navigate({
                to: "/squads/$squadId",
                params: { squadId: String(n) },
              })
            }
          />
        ))}
      </div>
    </div>
  );
}
