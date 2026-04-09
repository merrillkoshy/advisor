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

import { gearsQuery } from "@/api/gears";
import { Button } from "@/components/ui/button";
import { useGears } from "@/hooks/useGears";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/gears")({
  loader: async ({ context }) => {
    await context.queryClient.ensureQueryData(gearsQuery);
  },
  component: GearComponent,
});

function GearComponent() {
  const { data, isLoading, error } = useGears();
  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;

  return (
    <div className="w-full mx-auto">
      <FieldSet>
        <FieldLegend variant="label" className="text-center">
          Gear Details
        </FieldLegend>
        <FieldGroup className="items-center">
          <div className="grid grid-cols-2 gap-4">
            {data?.map((gear) => (
              <Field key={gear.id}>
                <Card className="w-full max-w-sm">
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
