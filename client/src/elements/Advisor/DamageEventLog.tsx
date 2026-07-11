import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import type { TickSnapshot } from "@/types";
import { useEffect, useRef } from "react";

const DamageEventLog = ({
  damageEvents,
}: {
  damageEvents: TickSnapshot["damageEvents"] | null;
}) => {
  const logRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (logRef.current) {
      logRef.current.scrollTop = logRef.current.scrollHeight;
    }
  }, [damageEvents]);
  return (
    <div className="flex flex-col gap-1 w-full">
      <Tabs defaultValue="overview" className="w-full">
        <TabsList>
          <TabsTrigger value="overview">Event Log</TabsTrigger>
          <TabsTrigger value="attacks">Attacks</TabsTrigger>
          <TabsTrigger value="damage">Damage taken</TabsTrigger>
          <TabsTrigger value="total-attack">Total Attacks</TabsTrigger>
          <TabsTrigger value="total-defense">Total Defense</TabsTrigger>
        </TabsList>
        <TabsContent value="overview">
          <Card>
            <CardHeader>
              <CardTitle>Event Log</CardTitle>
              <CardDescription>
                Track the sequence of damage events that occurred during the
                battle.
              </CardDescription>
            </CardHeader>
            <CardContent className="text-sm text-muted-foreground">
              <div
                ref={logRef}
                className="flex flex-col gap-0.5 font-mono text-[11px] h-48 overflow-y-auto scrollbar-thin scrollbar-thumb-white/10
        rounded-xl border border-white/10 bg-white/5 py-1 px-2
        "
              >
                {!damageEvents || damageEvents.length === 0 ? (
                  <span className="text-white/20">
                    Waiting for battle events...
                  </span>
                ) : (
                  damageEvents.map((event, index) => (
                    <div
                      key={index}
                      className="flex flex-wrap gap-1 items-center"
                    >
                      <span className="text-white font-bold">{`[ ${event?.timestamp?.toFixed(2)}s ]`}</span>
                      <span className="text-emerald-400">
                        {event.attackerName}
                      </span>
                      <span className="text-white/30">used</span>
                      <span className="text-yellow-400">{event.skillName}</span>
                      <span className="text-white/30">on</span>
                      <span className="text-red-400">{event.targetName}</span>
                      <span className="text-white/30">and dealt</span>
                      <span className="text-white font-bold">
                        {event.rawDamage.toFixed(1)}
                      </span>
                      <span className="text-white/30">dmg.</span>
                    </div>
                  ))
                )}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
        <TabsContent value="attacks">
          <Card>
            <CardHeader>
              <CardTitle>Total Attacks</CardTitle>
              <CardDescription>
                Track the number of attacks each hero has performed.
              </CardDescription>
            </CardHeader>
            <CardContent className="text-sm text-muted-foreground">
              <div className="text-white/30 h-48">
                {(() => {
                  const attackCounts: {
                    [key: string]: {
                      normal: number;
                      skill: number;
                    };
                  } = {};
                  damageEvents?.forEach((event) => {
                    if (!attackCounts[event.attackerName]) {
                      attackCounts[event.attackerName] = {
                        normal: 0,
                        skill: 0,
                      };
                    }
                    if (event.skillName === "Normal Attack") {
                      attackCounts[event.attackerName].normal += 1;
                    } else {
                      attackCounts[event.attackerName].skill += 1;
                    }
                  });
                  return Object.entries(attackCounts).map(([hero, count]) => (
                    <div
                      key={hero}
                      className="text-white/30 flex flex-wrap gap-1 items-center"
                    >
                      <span className="text-emerald-400">{hero}</span>
                      <span className="text-white/30">performed</span>
                      <span className="text-white font-bold">
                        {count?.normal ?? 0}
                      </span>
                      <span className="text-white/30">normal</span>
                      <span className="text-white/30">and</span>
                      <span className="text-white font-bold">
                        {count?.skill ?? 0}
                      </span>
                      <span className="text-white/30">skill-based attacks</span>
                    </div>
                  ));
                })()}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
        <TabsContent value="damage">
          <Card>
            <CardHeader>
              <CardTitle>Damage Taken</CardTitle>
              <CardDescription>
                Track the amount of damage each hero has taken.
              </CardDescription>
            </CardHeader>
            <CardContent className="text-sm text-muted-foreground">
              <div className="text-white/30 h-48">
                {(() => {
                  const damageTakenCounts: { [key: string]: number } = {};
                  damageEvents?.forEach((event) => {
                    damageTakenCounts[event.targetName] =
                      (damageTakenCounts[event.targetName] || 0) + 1;
                  });
                  return Object.entries(damageTakenCounts).map(
                    ([hero, count]) => (
                      <div
                        key={hero}
                        className="text-white/30 flex flex-wrap gap-1 items-center"
                      >
                        <span className="text-red-400">{hero}</span>
                        <span className="text-white/30">took</span>
                        <span className="text-white font-bold">{count}</span>
                        <span className="text-white/30">hits</span>
                      </div>
                    ),
                  );
                })()}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
        <TabsContent value="total-attack">
          <Card>
            <CardHeader>
              <CardTitle>Total Attacks</CardTitle>
              <CardDescription>
                Total attack damage yielded by each hero during the battle.
              </CardDescription>
            </CardHeader>

            <CardContent className="text-sm text-muted-foreground">
              <div className="text-white/30 h-48">
                {(() => {
                  const totalAttackDamage: { [key: string]: number } = {};
                  damageEvents?.forEach((event) => {
                    totalAttackDamage[event.attackerName] =
                      (totalAttackDamage[event.attackerName] || 0) +
                      event.rawDamage;
                  });
                  return Object.entries(totalAttackDamage).map(
                    ([hero, totalDamage]) => (
                      <div
                        key={hero}
                        className="text-white/30
                        flex
                        flex-wrap
                        gap-1
                        items-center"
                      >
                        <span className="text-emerald-400">{hero}</span>
                        <span className="text-white/30">dealt</span>
                        <span className="text-white font-bold">
                          {(totalDamage / 1000000).toFixed(1)}M
                        </span>
                        <span className="text-white/30">damage</span>
                      </div>
                    ),
                  );
                })()}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
        <TabsContent value="total-defense">
          <Card>
            <CardHeader>
              <CardTitle>Total Defense</CardTitle>
              <CardDescription>
                Total damage taken by each hero during the battle.
              </CardDescription>
            </CardHeader>
            <CardContent className="text-sm text-muted-foreground">
              <div className="text-white/30 h-48">
                {(() => {
                  const totalDamageTaken: { [key: string]: number } = {};
                  damageEvents?.forEach((event) => {
                    totalDamageTaken[event.targetName] =
                      (totalDamageTaken[event.targetName] || 0) +
                      event.rawDamage;
                  });
                  return Object.entries(totalDamageTaken).map(
                    ([hero, totalDamage]) => (
                      <div
                        key={hero}
                        className="text-white/30 flex flex-wrap gap-1 items-center"
                      >
                        <span className="text-red-400">{hero}</span>
                        <span className="text-white/30">took</span>
                        <span className="text-white font-bold">
                          {(totalDamage / 1000000).toFixed(1)}M
                        </span>
                        <span className="text-white/30">damage</span>
                      </div>
                    ),
                  );
                })()}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default DamageEventLog;
