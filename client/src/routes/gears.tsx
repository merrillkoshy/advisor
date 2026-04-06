import { getGears } from "@/api/gears";
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

import { Button } from "@/components/ui/button";
import type { Gear } from "@/types";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";

export const Route = createFileRoute("/gears")({
  component: GearComponent,
});

function GearComponent() {
  const [gears, setGears] = useState<Gear[]>([]);

  const gearsList = async () => {
    const response = await getGears();
    return response;
  };

  useEffect(() => {
    gearsList().then((g) => {
      console.log(g);
      setGears(g);
    });
  }, []);

  return (
    <div className="w-full mx-auto">
      <FieldSet>
        <FieldLegend variant="label" className="text-center">
          Gear Details
        </FieldLegend>
        <FieldGroup className="items-center">
          <div className="grid grid-cols-2 gap-4">
            {gears.map((gear) => (
              <Field>
                <Card className="w-full max-w-sm" key={gear.id}>
                  <CardHeader>
                    <CardTitle>{gear.baseName}</CardTitle>
                    <CardDescription>
                      {`Type: ${gear.type} | Base Power: ${gear.basePower}`}
                    </CardDescription>
                    <CardAction>
                      <Button variant="link">Edit</Button>
                    </CardAction>
                  </CardHeader>
                  <CardContent>
                    <p>{`Stats: ${gear.stats.map((stat) => `${stat.statKey.description}: ${stat.baseValue}`).join(", ")}`}</p>
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
