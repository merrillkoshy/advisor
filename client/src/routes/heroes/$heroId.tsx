import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Table } from "@/elements/UI/Table";
import { useHero } from "@/hooks/useHeroes";
import type { SkillEffect } from "@/types";
import { groupBy } from "@/utils/groupBy";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/heroes/$heroId")({
  component: RouteComponent,
});

function RouteComponent() {
  const { heroId } = Route.useParams();
  const { data } = useHero(Number(heroId));

  return (
    <div className="flex flex-col gap-6 p-6 mx-auto">
      <div className="flex flex-col gap-4">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold text-white">Hero Details</h1>
          <button className="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600">
            Edit Hero
          </button>
        </div>
        <div className="flex items-center gap-4">
          <img
            src={data?.imageUrl ?? "https://via.placeholder.com/150"}
            alt={data?.name}
            className="w-24 h-24 rounded-sm object-cover"
          />
          <div>
            <h2 className="text-xl font-semibold text-white">{data?.name}</h2>
            <p className="text-sm text-white/70">Rank: {data?.rank}</p>
            <p className="text-sm text-white/70">Type: {data?.type}</p>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div className="flex flex-col gap-1">
            <span className="text-sm text-white/70">HP</span>
            <span className="text-lg font-bold text-white">
              {(data?.hp ?? 0) / 1000} k
            </span>
          </div>
          <div className="flex flex-col gap-1">
            <span className="text-sm text-white/70">Attack</span>
            <span className="text-lg font-bold text-white">
              {(data?.atk ?? 0) / 1000} k
            </span>
          </div>
          <div className="flex flex-col gap-1">
            <span className="text-sm text-white/70">Defense</span>
            <span className="text-lg font-bold text-white">
              {(data?.def ?? 0) / 1000} k
            </span>
          </div>
          <div className="flex flex-col gap-1">
            <span className="text-sm text-white/70">Speed</span>
            <span className="text-lg font-bold text-white">{data?.spd}</span>
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <span className="text-lg font-bold text-white">Skills</span>
        </div>
        <div className="flex flex-col gap-1">
          {data?.skills
            ?.sort((a, b) => a.priority - b.priority)
            ?.map((skill, index) => {
              const skillEffectsGrouped = groupBy<SkillEffect, "level">(
                skill.effects,
                "level",
              );

              return (
                <Card className="w-full max-w-xl" key={index}>
                  <CardHeader>
                    <CardTitle>{skill.name}</CardTitle>
                    <CardDescription>{skill.description}</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Table
                      data={skillEffectsGrouped}
                      removeKeys={["id", "boolValue"]}
                    />
                    {/* {
                    "boolValue": null,
                    "id": 347,
                    "level": 20,
                    "statKey": {
                        "category": "Support",
                        "description": "Team-wide HP heal percentage",
                        "id": 30,
                        "key": "team_heal_hp_percent",
                        "valueType": "float"
                    },
                    "value": 12
                }, */}
                  </CardContent>
                  <CardFooter>Cooldown:{skill.cooldown ?? 0}</CardFooter>
                </Card>
              );
            })}
        </div>
      </div>
    </div>
  );
}
