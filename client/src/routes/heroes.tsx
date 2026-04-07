import { getHeroes } from "@/api/heroes";
import NoHero from "@/assets/icons/no-hero.svg";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
} from "@/components/ui/card";
import {
  Field,
  FieldGroup,
  FieldLegend,
  FieldSet,
} from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import type { Hero } from "@/types";
import { rankColorMap, typeIconMap } from "@/utils/typeIconMap";
import { createFileRoute } from "@tanstack/react-router";
import { ChevronsRight, Flame, Heart, Shield, Sword } from "lucide-react";
import { useEffect, useState } from "react";

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
          <span className="text-2xl">Heroes</span>
        </FieldLegend>
        <Separator className="my-4" />
        <FieldGroup className="items-center">
          <div className="grid grid-cols-2 gap-4">
            {heroes.map((hero) => (
              <Field>
                <Card className="w-full max-w-lg min-w-md" key={hero.id}>
                  <CardHeader>
                    <CardDescription>
                      <div className="flex items-start gap-4">
                        {hero.imageUrl ? (
                          <img
                            src={hero.imageUrl}
                            alt={hero.name}
                            className="w-16 h-20 object-cover rounded-b-sm"
                          />
                        ) : (
                          <img
                            src={NoHero}
                            alt={"No Image Available"}
                            className="w-16 h-20 object-cover rounded-b-sm"
                          />
                        )}
                        {typeIconMap[hero.type] && (
                          <img
                            src={typeIconMap[hero.type]}
                            alt={hero.type}
                            className="w-6 h-6 shrink-0"
                          />
                        )}
                        <span className="font-extrabold text-xl text-zinc-800">
                          {hero.name}
                        </span>
                        <p
                          className={`font-extrabold text-outline text-xl ${rankColorMap[hero.rank] ?? "text-white"}`}
                        >{`${hero.rank}`}</p>
                      </div>
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="flex items-center gap-2">
                      <div className="flex items-center gap-2">
                        <Heart className="w-6 h-6 shrink-0" />
                        <span>{`${hero.hp}`}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <Sword className="w-6 h-6 shrink-0" />
                        <span>{`${hero.atk}`}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <Shield className="w-6 h-6 shrink-0" />
                        <span>{`${hero.def}`}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <Flame className="w-6 h-6 shrink-0" />
                        <span>{`${hero.power}`}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <ChevronsRight className="w-6 h-6 shrink-0" />
                        <span>{`${hero.spd}`}</span>
                      </div>
                    </div>
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
