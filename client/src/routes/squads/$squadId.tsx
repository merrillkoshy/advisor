// routes/squads/$squadId.tsx
import { SquadEditor } from "@/elements/SquadEditor";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { ChevronLeft } from "lucide-react";

export const Route = createFileRoute("/squads/$squadId")({
  component: SquadEditorPage,
});

function SquadEditorPage() {
  const { squadId } = Route.useParams();
  const navigate = useNavigate();

  return (
    <div className="p-8">
      <button
        onClick={() => navigate({ to: "/squads" })}
        className="flex items-center gap-1 text-sm text-white/40 hover:text-white/80 transition-colors mb-6"
      >
        <ChevronLeft className="w-4 h-4" />
        Squads
      </button>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white">Squad {squadId}</h1>
        <p className="mt-1 text-sm text-white/40">
          Configure your formation and gear loadout.
        </p>
      </div>
      <SquadEditor squadId={squadId} />
    </div>
  );
}
