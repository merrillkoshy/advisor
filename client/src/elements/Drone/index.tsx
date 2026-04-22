import { Button } from "@/components/ui/button";
import type { Drone, DroneComponent } from "@/types";
import { useState } from "react";
import { DroneComponentSlot } from "./DroneComponentSlot";
import { DroneHub } from "./DroneHub";

interface DroneComponentInterface {
  data: Drone;
  saveDrone: (level: number) => void;
  saveDroneComponents: (components: DroneComponent[]) => void;
}
export default function Drone({
  data,
  saveDrone,
  saveDroneComponents,
}: DroneComponentInterface) {
  const { drone, components: c } = data;

  const [droneLevel, setDroneLevel] = useState<number | readonly number[]>(
    drone.level,
  );
  const [components, setComponents] = useState(c);

  const [LEFT_INDICES, RIGHT_INDICES] = [
    components.slice(0, 3),
    components.slice(3),
  ];
  const handleDroneChange = () => {
    if (droneLevel !== drone.level) saveDrone(droneLevel as number);
    if (
      c.map((item) => item.level).toString() !==
      components.map((item) => item.level).toString()
    )
      saveDroneComponents(components);
  };
  const handleComponentLevel = (componentId: number, level: number) => {
    setComponents((prev) =>
      prev.map((c) =>
        c.droneComponent.id === componentId ? { ...c, level } : c,
      ),
    );
  };

  return (
    <div className="flex flex-col gap-6 p-6 mx-auto">
      <div className="flex items-center justify-between">
        <h1 className="text-sm font-medium text-white/70 tracking-widest uppercase">
          Drone
        </h1>
        <Button onClick={handleDroneChange} className={"cursor-pointer"}>
          Save
        </Button>
      </div>

      <div className="flex items-center justify-between gap-4">
        {/* Left column */}
        <div className="flex flex-col gap-3">
          {LEFT_INDICES.map((component) => (
            <DroneComponentSlot
              key={component.droneComponent.id}
              name={component.droneComponent.name}
              imageUrl={component.droneComponent.imageUrl}
              level={component.level}
              maxLevel={component.droneComponent.maxLevel}
              onLevelChange={(level) =>
                handleComponentLevel(component.droneComponent.id, level)
              }
              side="left"
            />
          ))}
        </div>

        {/* Center hub */}
        <DroneHub level={droneLevel} onLevelChange={setDroneLevel} />

        {/* Right column */}
        <div className="flex flex-col gap-3">
          {RIGHT_INDICES.map((component) => (
            <DroneComponentSlot
              key={component.droneComponent.id}
              name={component.droneComponent.name}
              imageUrl={component.droneComponent.imageUrl}
              level={component.level}
              maxLevel={component.droneComponent.maxLevel}
              onLevelChange={(level) =>
                handleComponentLevel(component.droneComponent.id, level)
              }
              side="right"
            />
          ))}
        </div>
      </div>
    </div>
  );
}
