import { droneQuery, saveDrone, saveDroneComponents } from "@/api/drone";
import { APP_PATHS, PLAYER_ID } from "@/constants";
import type { DroneComponentRequest, DroneRequest } from "@/dto";
import Drone from "@/elements/Drone";

import { SkeletonLoader } from "@/elements/SkeletonLoader";
import { useDrone } from "@/hooks/useDrone";
import type { DroneComponent } from "@/types";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { createFileRoute, useNavigate } from "@tanstack/react-router";

export const Route = createFileRoute("/drone")({
  loader: async ({ context }) => {
    await context.queryClient.ensureQueryData(droneQuery(PLAYER_ID));
  },
  component: DronePage,
});

function DronePage() {
  const { data, isLoading, error } = useDrone({
    playerId: PLAYER_ID,
  });
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { mutate: droneMutation } = useMutation({
    mutationFn: (drone: DroneRequest) => saveDrone(PLAYER_ID, drone.droneLevel),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["drone", PLAYER_ID] });
      navigate({ to: APP_PATHS.drone });
    },
    onError: (error) => {
      console.error(error);
    },
  });

  const { mutate: droneComponentMutation } = useMutation({
    mutationFn: (components: DroneComponentRequest[]) =>
      saveDroneComponents(PLAYER_ID, components),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["drone", PLAYER_ID] });
      navigate({ to: APP_PATHS.drone });
    },
    onError: (error) => {
      console.error(error);
    },
  });

  console.log(data);

  const onSaveDrone = (droneLevel: number) => {
    droneMutation({ droneLevel });
  };

  const onSaveDroneComponents = (components: DroneComponent[]) => {
    const mutableComponents: DroneComponentRequest[] = components.map(
      (component) => {
        return {
          droneComponentId: component.droneComponent.id,
          droneComponentLevel: component.level,
        };
      },
    );
    droneComponentMutation(mutableComponents);
  };

  if (isLoading) return <SkeletonLoader items={6} />;
  if (error) return <div>Something went wrong</div>;

  return (
    <div>
      {data && (
        <Drone
          data={data}
          saveDrone={onSaveDrone}
          saveDroneComponents={onSaveDroneComponents}
        />
      )}
    </div>
  );
}
