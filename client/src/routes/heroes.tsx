import { getHeroes } from "@/api/heroes";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Field,
  FieldGroup,
  FieldLegend,
  FieldSet,
} from "@/components/ui/field";
import type { Hero } from "@/types";

export const Route = createFileRoute("/heroes")({
  component: HeroesPage,
});

function HeroesPage() {
  const [heroes, setHeroes] = useState<Hero[]>([]);

  const heroesList = async () => {
    const response = await getHeroes();
    return response;
  };

  useEffect(() => {
    heroesList().then((hro) => {
      console.log(hro);
      setHeroes(hro);
    });
  }, []);

  return (
    <div className="w-full mx-auto">
      <FieldSet>
        <FieldLegend variant="label" className="text-center">
          Hero Details
        </FieldLegend>
        <FieldGroup className="items-center">
          <div className="grid grid-cols-2 gap-4">
            {heroes.map((hero) => (
              <Field>
                <Card className="w-full max-w-sm" key={hero.id}>
                  <CardHeader>
                    <CardTitle>{hero.name}</CardTitle>
                    <CardDescription>
                      {`Type: ${hero.type} | Rank: ${hero.rank} | Tier: ${hero.tier}`}
                    </CardDescription>
                    <CardAction>
                      <Button variant="link">Edit</Button>
                    </CardAction>
                  </CardHeader>
                  <CardContent>
                    <p>{`HP: ${hero.hp} | ATK: ${hero.atk} | DEF: ${hero.def} | SPD: ${hero.spd} | Power: ${hero.power}`}</p>
                  </CardContent>
                </Card>
              </Field>
            ))}
          </div>
        </FieldGroup>
      </FieldSet>
    </div>
  );
}
