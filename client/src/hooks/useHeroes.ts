import { heroesQuery, heroQuery } from "@/api/heroes";
import { useQuery } from "@tanstack/react-query";

export function useHeroes() {
  return useQuery(heroesQuery);
}
export function useHero(heroId: number) {
  return useQuery(heroQuery(heroId));
}
